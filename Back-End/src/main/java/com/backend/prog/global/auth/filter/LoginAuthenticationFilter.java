package com.backend.prog.global.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Map;

@Log4j2
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public LoginAuthenticationFilter() {
        super(new AntPathRequestMatcher("/members/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if(request.getContentType() == null || !request.getContentType().equals("application/json")) {
            throw new AuthenticationServiceException("Content-Type not supported");
        }

        Map<String, String> jsonData = new ObjectMapper().readValue(request.getInputStream(), Map.class);

        String email = jsonData.get("email");
        String password = jsonData.get("password");

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }
}
