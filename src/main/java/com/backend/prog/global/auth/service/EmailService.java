package com.backend.prog.global.auth.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    void sendEmail(String email, String authCode);

    SimpleMailMessage createEmailForm(String toEmail, String authCode);
}
