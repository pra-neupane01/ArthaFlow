package com.arthaflow.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class AppConfig {
    private static final Map<String, String> DOTENV_VALUES = loadDotEnv();

    private AppConfig() {
    }

    public static String get(String key) {
        return get(key, null);
    }

    public static String get(String key, String defaultValue) {
        String envValue = System.getenv(key);
        if (hasText(envValue)) {
            return envValue;
        }

        String dotenvValue = DOTENV_VALUES.get(key);
        if (hasText(dotenvValue)) {
            return dotenvValue;
        }

        return defaultValue;
    }

    private static Map<String, String> loadDotEnv() {
        Map<String, String> values = new HashMap<>();
        Path envPath = findDotEnvPath();
        if (envPath == null) {
            return values;
        }

        try (BufferedReader reader = Files.newBufferedReader(envPath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line, values);
            }
        } catch (IOException ignored) {
        }

        return values;
    }

    private static Path findDotEnvPath() {
        String configuredPath = System.getenv("ARTHAFLOW_ENV_FILE");
        if (hasText(configuredPath)) {
            Path path = Paths.get(configuredPath);
            if (Files.isRegularFile(path)) {
                return path;
            }
        }

        Path current = Paths.get("").toAbsolutePath();
        Path foundFromWorkingDirectory = findInCurrentOrParents(current);
        if (foundFromWorkingDirectory != null) {
            return foundFromWorkingDirectory;
        }

        Path classLocation = getClassLocation();
        Path foundFromClassLocation = findInCurrentOrParents(classLocation);
        if (foundFromClassLocation != null) {
            return foundFromClassLocation;
        }

        return null;
    }

    private static Path findInCurrentOrParents(Path start) {
        Path current = start;
        for (int i = 0; i < 6 && current != null; i++) {
            Path candidate = current.resolve(".env");
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
            current = current.getParent();
        }

        return null;
    }

    private static Path getClassLocation() {
        try {
            Path location = Paths.get(AppConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            return Files.isRegularFile(location) ? location.getParent() : location;
        } catch (URISyntaxException | NullPointerException | SecurityException e) {
            return null;
        }
    }

    private static void parseLine(String rawLine, Map<String, String> values) {
        String line = rawLine.trim();
        if (line.isEmpty() || line.startsWith("#")) {
            return;
        }
        if (line.startsWith("export ")) {
            line = line.substring("export ".length()).trim();
        }

        int equalsIndex = line.indexOf('=');
        if (equalsIndex <= 0) {
            return;
        }

        String key = line.substring(0, equalsIndex).trim();
        String value = line.substring(equalsIndex + 1).trim();
        if (key.isEmpty()) {
            return;
        }

        values.put(key, stripQuotes(value));
    }

    private static String stripQuotes(String value) {
        if (value.length() >= 2) {
            char first = value.charAt(0);
            char last = value.charAt(value.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                return value.substring(1, value.length() - 1);
            }
        }
        return value;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
