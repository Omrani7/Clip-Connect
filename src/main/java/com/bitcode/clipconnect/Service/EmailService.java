package com.bitcode.clipconnect.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String toEmail, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("clipndconnect@outlook.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        emailSender.send(message);

        System.out.println("Message sent successfully");
    }
    public void sendHtmlVerificationEmail(String toEmail, String verificationCode) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("clipndconnect@outlook.com");
        helper.setTo(toEmail);
        helper.setSubject("Email Verification");

        // Load HTML content from template file
        String htmlContent = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Email Verification</title>"
                + "<style>"
                + "/* CSS styles */"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<h1>Email Verification</h1>"
                + "<p>Please use the following verification code to verify your email:</p>"
                + "<p class=\"verification-code\">" + verificationCode + "</p>"
                + "<p>If you didn't request this verification, please ignore this email.</p>"
                + "</div>"
                + "<div class=\"footer\">"
                + "<p>This email was sent by YourAppName.</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setText(htmlContent, true); // Set the HTML content to true

        emailSender.send(message);

        System.out.println("HTML verification email sent successfully");
    }

}