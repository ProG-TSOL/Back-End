package com.backend.prog.global.auth.handler;

import com.backend.prog.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtUtil jwtUtil;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            String refreshToken = jwtUtil.extractRefreshToken(request.getCookies());

            Map<String, Object> refreshClaim = jwtUtil.extractClaim(refreshToken);

            Integer id = (Integer) refreshClaim.get("id");

            jwtUtil.destroyRefreshToken(response, id);

            String accessToken = jwtUtil.extractAccessToken(request);

            Map<String, Object> claim = jwtUtil.extractClaim(accessToken);

            Integer exp = (Integer) claim.get("exp");

            jwtUtil.destroyAccessToken(accessToken, id, jwtUtil.getExpiration(exp));
        }  catch (Exception exception) {
        }
    }
}