package com.backend.prog.global.util;

import com.backend.prog.global.auth.dao.BlacklistRepository;
import com.backend.prog.global.auth.dao.RefreshTokenRepository;
import com.backend.prog.global.auth.domain.Blacklist;
import com.backend.prog.global.auth.domain.RefreshToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final Map<String, Object> HEADERS = new HashMap<>(){{
        put("typ", "JWT"); put("alg", "HS256");
    }};

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.header}")
    private String accessTokenHeader;

    @Value("${jwt.access.expiration}")
    private Integer accessTokenExpiration;

    @Value("${jwt.refresh.header}")
    private String refreshTokenHeader;

    @Value("${jwt.refresh.expiration}")
    private Integer refreshTokenExpiration;

    @Value("${jwt.authorization.header}")
    public String authorizationHeader;

    @Value("${jwt.authorization.prefix}")
    public String prefix;

    private final RefreshTokenRepository refreshTokenRepository;

    private final BlacklistRepository blackListRepository;

    public void generateAccessToken(HttpServletResponse response, Map<String, Object> claim) {
        String accessToken = Jwts.builder()
                .setHeader(HEADERS)
                .setClaims(claim)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(accessTokenExpiration).toInstant()))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();

        response.setHeader(accessTokenHeader, accessToken);
    }

    public void generateRefreshToken(HttpServletResponse response, Map<String, Object> claim) {
        String refreshToken = Jwts.builder()
                .setHeader(HEADERS)
                .setClaims(claim)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(refreshTokenExpiration).toInstant()))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();

        refreshTokenRepository.save(RefreshToken.builder()
                .id((Integer) claim.get("id"))
                .refreshToken(refreshToken)
                .expiration(refreshTokenExpiration)
                .build());

        ResponseCookie cookie = ResponseCookie.from(refreshTokenHeader, refreshToken)
                .maxAge(refreshTokenExpiration)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();

        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public String extractAccessToken(HttpServletRequest request) {
        String headerStr = request.getHeader(authorizationHeader);
        String type = headerStr.substring(0, 6);
        String token = headerStr.substring(7);

        if(!type.equalsIgnoreCase(prefix) || isBlackList(token)) {
            return null;
        }

        return token;
    }

    public String extractRefreshToken(Cookie[] cookies) {
        for (Cookie c : cookies) {
            if (c.getName().equals(refreshTokenHeader)) {
                return c.getValue();
            }
        }

        return null;
    }

    public Map<String, Object> extractClaim(String token) {
        Map<String, Object> claim = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claim;
    }

    public Integer getExpiration(Integer exp) {
        Date current = new Date(System.currentTimeMillis());

        Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);

        return (int)(expTime.getTime() - current.getTime());
    }

    public boolean isBlackList(String token) {
        return blackListRepository.findByAccessToken(token).isPresent();
    }

    public boolean existRefreshToken(Integer id, String token) {
        return refreshTokenRepository.findById(id).orElseThrow().getRefreshToken().equals(token);
    }

    public void destroyToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshToken(request.getCookies());

        Map<String, Object> refreshClaim = extractClaim(refreshToken);

        Integer id = (Integer) refreshClaim.get("id");

        destroyRefreshToken(response, id);

        String accessToken = extractAccessToken(request);

        Map<String, Object> accessClaim = extractClaim(accessToken);

        Integer exp = (Integer) accessClaim.get("exp");

        destroyAccessToken(accessToken, id, getExpiration(exp));
    }

    public void destroyAccessToken(String token, Integer id, Integer exp) {
        blackListRepository.save(Blacklist.builder()
                .id(id)
                .accessToken(token)
                .expiration(exp)
                .build());
    }

    public void destroyRefreshToken(HttpServletResponse response ,Integer id) {
        refreshTokenRepository.deleteById(id);

        ResponseCookie cookie = ResponseCookie.from(refreshTokenHeader, "")
                .maxAge(0)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }
}