package com.backend.prog.global.auth.filter;

import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import com.backend.prog.global.util.JwtUtil;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private static final String[] TOKEN_CHECK_EXCLUDE_PATTERNS = {"/api/members/login", "/api/members/sign-up", "/api/members/nickname-validation-check"
            , "/api/members/email-verification", "/api/members/email-verification-confirm", "/api/members/profile/", "/api/members/detail-profile/", "/api/oauth2/authorization/", "/api/login/oauth2/code/"
            , "/api/codes", "/api/codes/", "/api/codes/details/", "/api/codes/details/detail/"};

    private static final String[][] TOKEN_CHECK_EXCLUDE_PATTERNS_AND_METHOD = {{"GET", "/api/comments"}, {"GET", "/api/projects"}};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        for(String PATTERN : TOKEN_CHECK_EXCLUDE_PATTERNS) {
            if(path.contains(PATTERN)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        for(String[] str : TOKEN_CHECK_EXCLUDE_PATTERNS_AND_METHOD) {
            if(method.contains(str[0]) && path.contains(str[1])) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        try {
            String token = jwtUtil.extractAccessToken(request);

            Map<String, Object> claim = jwtUtil.extractClaim(token);

            Integer id = (Integer) claim.get("id");

            List<String> authorities = (List<String>) claim.get("authorities");

            List<SimpleGrantedAuthority> authoritiesList = authorities.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority))
                    .collect(Collectors.toList());

            Authentication authentication = new UsernamePasswordAuthenticationToken(id, null, authoritiesList);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CommonException(ExceptionEnum.EXPIRED_ACCESS_TOKEN);
        } catch (Exception exception) {
            throw new CommonException(ExceptionEnum.INVALID_ACCESS_TOKEN);
        }
    }
}