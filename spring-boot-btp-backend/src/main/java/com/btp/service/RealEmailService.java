package com.btp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

/**
 * Real SMTP email service — only active when spring.mail.password is set.
 * Falls back to LoggingEmailService when no mail credentials are configured.
 *
 * Both send methods are @Async (fire-and-forget) so email failures never block
 * the calling request. If sending fails, the link is logged so an admin can
 * share it manually.
 */
@Slf4j
@Service
@Primary
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.email.enabled", havingValue = "true")
public class RealEmailService implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Async
    @Override
    public void sendInvitationEmail(String toEmail, String activationLink) {
        try {
            MimeMessage message = buildMessage(
                toEmail,
                "Invitation à rejoindre la plateforme GestionBTP",
                buildHtml("invitation-email.html", "activation_link", activationLink)
            );
            mailSender.send(message);
            log.info("Invitation email sent to {}", toEmail);
        } catch (Exception e) {
            // Never crash user creation because of an email failure.
            // Log the link so an admin can share it manually if needed.
            log.warn("Failed to send invitation email to {} — activation link: {} | error: {}",
                toEmail, activationLink, e.getMessage());
        }
    }

    @Async
    @Override
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        try {
            MimeMessage message = buildMessage(
                toEmail,
                "Réinitialisation de votre mot de passe GestionBTP",
                buildHtml("reset-password.html", "reset_link", resetLink)
            );
            mailSender.send(message);
            log.info("Password reset email sent to {}", toEmail);
        } catch (Exception e) {
            log.warn("Failed to send password reset email to {} — reset link: {} | error: {}",
                toEmail, resetLink, e.getMessage());
        }
    }

    private MimeMessage buildMessage(String to, String subject, String html) throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
            msg, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name()
        );
        helper.setFrom(fromAddress);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        return msg;
    }

    private String buildHtml(String template, String varKey, String varValue) {
        Map<String, Object> vars = new HashMap<>();
        vars.put(varKey, varValue);
        Context ctx = new Context();
        ctx.setVariables(vars);
        return templateEngine.process(template, ctx);
    }
}
