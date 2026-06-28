package com.arthaflow.util;

import com.arthaflow.dao.UserDAO;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.net.IDN;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Pattern;

public class ValidationService {
    private static final UserDAO userDAO = new UserDAO();
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^(?=.{1,254}$)(?=.{1,64}@)[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+"
                    + "(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
                    + "(?:[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+[A-Za-z]{2,63}$"
    );

    public static boolean isValidEmail(String email){
        if (email == null) {
            return false;
        }

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            return false;
        }

        String domain = normalizedEmail.substring(normalizedEmail.lastIndexOf('@') + 1);
        String asciiDomain;
        try {
            asciiDomain = IDN.toASCII(domain);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return hasMailExchangeRecord(asciiDomain);
    }

    private static boolean hasMailExchangeRecord(String domain) {
        Hashtable<String, String> env = new Hashtable<>();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        env.put("com.sun.jndi.dns.timeout.initial", "1500");
        env.put("com.sun.jndi.dns.timeout.retries", "1");

        InitialDirContext context = null;
        try {
            context = new InitialDirContext(env);
            Attributes attributes = context.getAttributes(domain, new String[]{"MX"});
            Attribute mxRecords = attributes.get("MX");
            if (mxRecords == null || mxRecords.size() == 0) {
                return false;
            }

            for (int i = 0; i < mxRecords.size(); i++) {
                String record = String.valueOf(mxRecords.get(i)).trim();
                if (!record.endsWith(" .") && !".".equals(record)) {
                    return true;
                }
            }
        } catch (NamingException e) {
            return false;
        } finally {
            if (context != null) {
                try {
                    context.close();
                } catch (NamingException ignored) {
                }
            }
        }

        return false;
    }

    public static boolean isValidphoneNumber(String phoneNumber){
        String phoneNumberRegex = "^\\+977\\s?(97|98)[0-9]{8}$|^(97|98)[0-9]{8}$";
        if (phoneNumber != null && phoneNumber.matches(phoneNumberRegex)){
            return true;
        }
        return false;
    }





    public static boolean isValidPassword(String password){
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (password != null && password.matches(passwordRegex)){
            return true;
        }
        return false;
    }

    public static boolean isEmailExists(String email){
        if(email == null){
            return false;
        }
        return userDAO.emailExists(email);
    }
}

