package com.btp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Dev-only email stub. Active when app.email.enabled != true.
 * Prints the activation / reset link to the console so developers
 * can manually share it without needing SMTP credentials.
 */
@Slf4j
@Service
// Active when app.email.enabled=false OR the property is absent (i.e. dev mode)
@ConditionalOnProperty(name = "app.email.enabled", havingValue = "false", matchIfMissing = true)
public class LoggingEmailService implements EmailService {

    @Override
    public void sendInvitationEmail(String toEmail, String activationLink) {
        log.warn("============================================================");
        log.warn("  [DEV] Invitation email NOT sent (email disabled in dev)");
        log.warn("  To: {}", toEmail);
        log.warn("  Activation link: {}", activationLink);
        log.warn("============================================================");
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        log.warn("============================================================");
        log.warn("  [DEV] Password reset email NOT sent (email disabled in dev)");
        log.warn("  To: {}", toEmail);
        log.warn("  Reset link: {}", resetLink);
        log.warn("============================================================");
    }
}
