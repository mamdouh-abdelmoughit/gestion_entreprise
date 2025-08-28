
package com.btp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoggingEmailService implements EmailService {
    @Override
    public void sendInvitationEmail(String toEmail, String activationLink) {
        log.info("SIMULATED EMAIL to {}: Activate your account here: {}", toEmail, activationLink);
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        log.info("SIMULATED EMAIL to {}: Reset your password here: {}", toEmail, resetLink);
    }
}
