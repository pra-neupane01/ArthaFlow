package com.arthaflow.service;

import com.arthaflow.util.AppConfig;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {
    private final String host = AppConfig.get("ARTHAFLOW_SMTP_HOST", "smtp.gmail.com");
    private final String port = AppConfig.get("ARTHAFLOW_SMTP_PORT", "587");
    private final String username = AppConfig.get("ARTHAFLOW_SMTP_USERNAME");
    private final String password = normalizeSmtpPassword(AppConfig.get("ARTHAFLOW_SMTP_PASSWORD"));
    private final String from = AppConfig.get("ARTHAFLOW_MAIL_FROM", username);
    private final String authEnabled = AppConfig.get("ARTHAFLOW_SMTP_AUTH", "true");
    private final String startTlsEnabled = AppConfig.get("ARTHAFLOW_SMTP_STARTTLS_ENABLE", "true");

    public void sendRegistrationOtp(String recipientEmail, String fullName, String otp) throws MessagingException {
        if (!isConfigured()) {
            throw new MessagingException("SMTP is not configured. Set ARTHAFLOW_SMTP_USERNAME and ARTHAFLOW_SMTP_PASSWORD in .env.");
        }

        Message message = new MimeMessage(createSession());
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject("Your ArthaFlow verification code");
        message.setText(buildOtpEmail(fullName, otp));

        Transport.send(message);
    }

    private Session createSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", authEnabled);
        properties.put("mail.smtp.starttls.enable", startTlsEnabled);
        properties.put("mail.smtp.connectiontimeout", "10000");
        properties.put("mail.smtp.timeout", "10000");

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private String buildOtpEmail(String fullName, String otp) {
        String greetingName = fullName == null || fullName.isBlank() ? "customer" : fullName.trim();
        return "Namaste " + greetingName + ",\n\n"
                + "Your ArthaFlow Bank registration verification code is: " + otp + "\n\n"
                + "This code expires in 10 minutes. If you did not request this registration, ignore this email.\n\n"
                + "ArthaFlow Bank";
    }

    private boolean isConfigured() {
        return username != null && !username.isBlank()
                && password != null && !password.isBlank()
                && from != null && !from.isBlank();
    }

    private String normalizeSmtpPassword(String rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        if (host != null && host.toLowerCase().contains("gmail.com")) {
            return rawPassword.replaceAll("\\s+", "");
        }
        return rawPassword;
    }

}
