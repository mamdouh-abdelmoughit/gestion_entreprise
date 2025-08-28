package com.btp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Primary
public class RealEmailService implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    // By making this method @Async, it will run in a background thread
    // so the user doesn't have to wait for the email to send.
    @Async
    @Override
    public void sendInvitationEmail(String toEmail, String activationLink) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED,
                    StandardCharsets.UTF_8.name()
            );

            Map<String, Object> properties = new HashMap<>();
            properties.put("activation_link", activationLink);

            Context context = new Context();
            context.setVariables(properties);

            helper.setFrom("mamadouabdo29@gmail.com"); // Your "from" address
            helper.setTo(toEmail);
            helper.setSubject("Invitation à rejoindre la plateforme GestionBTP");

            // You will need to create an HTML template for this email
            String htmlTemplate = templateEngine.process("invitation-email.html", context);
            helper.setText(htmlTemplate, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            // In a real app, you'd have more robust error logging here
            throw new RuntimeException("Failed to send invitation email", e);
        }
    }

    // You would create a similar method for sendPasswordResetEmail
    @Async
    @Override
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        // ... similar logic to the method above, but using a different template ...
        System.out.println("Password reset email sending is not implemented yet.");
    }
}