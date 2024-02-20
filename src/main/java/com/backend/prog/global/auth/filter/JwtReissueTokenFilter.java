package com.backend.prog.global.auth.filter;

import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import com.backend.prog.global.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtReissueTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private static final String PATTERN = "/api/members/reissue-token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if(!path.contains(PATTERN)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = jwtUtil.extractRefreshToken(request.getCookies());

            Map<String, Object> claim = jwtUtil.extractClaim(token);

            Integer id = (Integer) claim.get("id");

            if(!jwtUtil.existRefreshToken(id, token)) {
                throw new Exception();
            }

            Integer exp = (Integer) claim.get("exp");
            Object authorities = claim.get("authorities");

            Map<String, Object> newClaim = new HashMap<>();

            newClaim.put("id", id);
            newClaim.put("authorities", authorities);

            jwtUtil.generateAccessToken(response, newClaim);

            if(jwtUtil.getExpiration(exp) <= 604800000) {
                jwtUtil.generateRefreshToken(response, newClaim);
            }
        } catch (ExpiredJwtException e) {
            throw new CommonException(ExceptionEnum.EXPIRED_REFRESH_JWT);
        } catch (Exception e) {
            throw new CommonException(ExceptionEnum.INVALID_REFRESH_TOKEN);
        }
    }
}