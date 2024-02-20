package com.backend.prog.global.auth.service;

import com.backend.prog.global.auth.dao.EmailAuthRepository;
import com.backend.prog.global.auth.domain.EmailAuth;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final EmailAuthRepository emailAuthRepository;

    private static final String TITLE = "PROG 가입을 위한 인증번호입니다.";

    private static final String CONTENT = "인증번호: ";

    @Value("${spring.mail.auth-code-expiration-millis}")
    private Integer EXPIRATION;

    @Transactional
    public void sendEmail(String email, String authCode) {
        try {
            SimpleMailMessage emailForm = createEmailForm(email, authCode);

            javaMailSender.send(emailForm);

            emailAuthRepository.save(EmailAuth.builder()
                    .id(authCode + email)
                    .authCode(authCode)
                    .expiration(EXPIRATION)
                    .build());
        } catch (RuntimeException e) {
            throw new CommonException(ExceptionEnum.INTERNAL_SERVER_ERROR);
        }
    }

    public SimpleMailMessage createEmailForm(String email, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject(TITLE);
        message.setText(CONTENT + authCode);

        return message;
    }
}