package com.backend.prog.global.auth.handler;

import com.backend.prog.global.error.ExceptionEnum;
import com.backend.prog.global.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final ResponseUtil responseUtil;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if(exception.getMessage().equals("withdrawal")) {
            responseUtil.setErrorResponse(response, ExceptionEnum.ALREADY_WITHDRAWAL_MEMBER);
        }
        else if(exception.getMessage().equals("already")) {
            responseUtil.setErrorResponse(response, ExceptionEnum.ALREADY_EXIST_EMAIL);
        }
        else {
            responseUtil.setErrorResponse(response, ExceptionEnum.LOGIN_FAILED);
        }
    }
}