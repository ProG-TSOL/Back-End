package com.backend.prog.global.auth.handler;

import com.backend.prog.global.auth.dto.MemberSecurityDTO;
import com.backend.prog.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();

        Map<String, Object> claim = new HashMap<>();

        claim.put("id", memberSecurityDTO.getId());
        claim.put("authorities", memberSecurityDTO.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        jwtUtil.generateAccessToken(response, claim);
        jwtUtil.generateRefreshToken(response, claim);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}