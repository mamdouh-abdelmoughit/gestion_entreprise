
package com.btp.service;

public interface EmailService {
    void sendInvitationEmail(String toEmail, String activationLink);
    void sendPasswordResetEmail(String toEmail, String resetLink);
}
