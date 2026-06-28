package com.arthaflow.service;

import com.arthaflow.util.AppConfig;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

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
        message.setContent(buildOtpContent(fullName, otp));

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

    private MimeMultipart buildOtpContent(String fullName, String otp) throws MessagingException {
        MimeMultipart multipart = new MimeMultipart("alternative");

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(buildPlainOtpEmail(fullName, otp), "UTF-8");
        multipart.addBodyPart(textPart);

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(buildHtmlOtpEmail(fullName, otp), "text/html; charset=UTF-8");
        multipart.addBodyPart(htmlPart);

        return multipart;
    }

    private String buildPlainOtpEmail(String fullName, String otp) {
        String greetingName = fullName == null || fullName.isBlank() ? "customer" : fullName.trim();
        return "Namaste " + greetingName + ",\n\n"
                + "Your ArthaFlow Bank registration verification code is: " + otp + "\n\n"
                + "This code expires in 10 minutes. If you did not request this registration, ignore this email.\n\n"
                + "ArthaFlow Bank";
    }

    private String buildHtmlOtpEmail(String fullName, String otp) {
        String greetingName = escapeHtml(fullName == null || fullName.isBlank() ? "customer" : fullName.trim());
        String safeOtp = escapeHtml(otp);

        return "<!DOCTYPE html>"
                + "<html><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head>"
                + "<body style=\"margin:0;padding:0;background:#f4f6f5;font-family:Arial,Helvetica,sans-serif;color:#1a2e22;\">"
                + "<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"background:#f4f6f5;padding:32px 12px;\">"
                + "<tr><td align=\"center\">"
                + "<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"max-width:560px;background:#ffffff;border:1px solid #d1e9dc;border-radius:12px;overflow:hidden;box-shadow:0 8px 24px rgba(6,62,39,0.12);\">"
                + "<tr><td style=\"background:#063e27;padding:28px 32px;\">"
                + "<table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\"><tr>"
                + "<td style=\"width:46px;height:46px;background:#16a364;border-radius:10px;text-align:center;color:#ffffff;font-size:22px;font-weight:800;line-height:46px;\">A</td>"
                + "<td style=\"padding-left:12px;\">"
                + "<div style=\"color:#ffffff;font-size:22px;font-weight:800;line-height:1.1;\">ArthaFlow</div>"
                + "<div style=\"color:#a8d5be;font-size:12px;font-weight:600;line-height:1.4;\">Digital Banking</div>"
                + "</td></tr></table>"
                + "</td></tr>"
                + "<tr><td style=\"padding:34px 32px 30px;\">"
                + "<div style=\"display:inline-block;background:#ecfdf5;border:1px solid #a7f3d0;border-radius:999px;color:#065f38;font-size:12px;font-weight:700;padding:6px 12px;margin-bottom:18px;\">Email Verification</div>"
                + "<h1 style=\"margin:0 0 12px;color:#1a2e22;font-size:24px;line-height:1.25;font-weight:800;\">Confirm your ArthaFlow registration</h1>"
                + "<p style=\"margin:0 0 22px;color:#5a7a6b;font-size:15px;line-height:1.7;\">Namaste " + greetingName + ", use this one-time code to verify your email and continue opening your ArthaFlow Bank account.</p>"
                + "<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin:0 0 24px;\"><tr><td align=\"center\" style=\"background:#f0faf5;border:1px dashed #0a7c4e;border-radius:10px;padding:22px;\">"
                + "<div style=\"color:#5a7a6b;font-size:12px;font-weight:700;margin-bottom:8px;\">Your verification code</div>"
                + "<div style=\"color:#063e27;font-size:38px;line-height:1;font-weight:800;letter-spacing:8px;\">" + safeOtp + "</div>"
                + "</td></tr></table>"
                + "<p style=\"margin:0 0 16px;color:#1a2e22;font-size:14px;line-height:1.7;\"><strong>This code expires in 10 minutes.</strong> Do not share it with anyone.</p>"
                + "<p style=\"margin:0;color:#5a7a6b;font-size:13px;line-height:1.7;\">If you did not request this registration, you can safely ignore this email.</p>"
                + "</td></tr>"
                + "<tr><td style=\"background:#f0faf5;border-top:1px solid #d1e9dc;padding:18px 32px;color:#5a7a6b;font-size:12px;line-height:1.6;\">"
                + "ArthaFlow Bank Ltd.<br>This is an automated security email."
                + "</td></tr>"
                + "</table>"
                + "</td></tr></table>"
                + "</body></html>";
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

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

}
