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
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private static final String[] EXCEPT_URL = {"/login", "/join"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        for(String URL : EXCEPT_URL) {
            if(path.contains(URL)) {
                filterChain.doFilter(request, response);
            }
        }

        try {
            String token = jwtUtil.extractAccessToken(request);

            Map<String, Object> claim = jwtUtil.extractClaim(token);

            Integer id = (Integer) claim.get("id");

            List<String> authorities = ((List<String>) claim.get("authorities"));

            setAuthenticatedUser(id, authorities);

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException expiredJwtException) {
            reissueToken(request, response);
        } catch (Exception exception) {
            throw new CommonException(ExceptionEnum.INVALID_ACCESS_TOKEN);
        }
    }

    protected void reissueToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = jwtUtil.extractRefreshToken(request.getCookies());

            Map<String, Object> claim = jwtUtil.extractClaim(token);

            Integer id = (Integer) claim.get("id");

            if(!jwtUtil.existRefreshToken(id, token)) {
                log.info(id + " " + token);
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

            setAuthenticatedUser(id, (List<String>) authorities);
        } catch (ExpiredJwtException e) {
            throw new CommonException(ExceptionEnum.EXPIRED_REFRESH_JWT);
        } catch (Exception e) {
            throw new CommonException(ExceptionEnum.INVALID_REFRESH_TOKEN);
        }
    }

    protected void setAuthenticatedUser(Integer id, List<String> authorities) {
        List<SimpleGrantedAuthority> authoritiesList = authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(id, null, authoritiesList);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
