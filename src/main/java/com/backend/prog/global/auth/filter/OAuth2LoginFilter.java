package com.backend.prog.global.auth.filter;

import com.backend.prog.global.auth.service.OAuth2MemberService;
import com.backend.prog.global.auth.api.OAuth2Api;
import com.backend.prog.global.auth.handler.LoginFailureHandler;
import com.backend.prog.global.auth.handler.LoginSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class OAuth2LoginFilter extends OncePerRequestFilter {

    private final LoginSuccessHandler loginSuccessHandler;

    private final LoginFailureHandler loginFailureHandler;

    private final Environment environment;

    private final ObjectMapper objectMapper;

    private final OAuth2Api oAuth2Api;

    private final OAuth2MemberService oAuth2MemberService;

    private static final String PATTERN = "/api/members/login/oauth2";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if(!path.contains(PATTERN)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String provider = path.substring(PATTERN.length() + 1);
            String code = request.getParameter("code");
            String clientId = environment.getProperty("oauth2." + provider + ".client-id");
            String clientSecret = environment.getProperty("oauth2." + provider + ".client-secret");

            URI accessTokenUri = new URI(environment.getProperty("oauth2." + provider + ".access-token-url"));
            String type = environment.getProperty("oauth2.type");
            Object accessTokenObject = oAuth2Api.getAccessToken(accessTokenUri, type, clientId, clientSecret, code);
            Map<String, Object> accessTokenMap = objectMapper.convertValue(accessTokenObject, Map.class);
            String prefix = environment.getProperty("oauth2.prefix");
            String accessToken = accessTokenMap.get("access_token").toString();

            URI userInfoUri = new URI(environment.getProperty("oauth2." + provider + ".information-url"));
            Object userInfoObject = oAuth2Api.getUserInfo(userInfoUri, prefix + " " + accessToken);
            Map<String, Object> userInfoMap = objectMapper.convertValue(userInfoObject, Map.class);
            String email = (userInfoMap.get("email") != null) ? userInfoMap.get("email").toString() : null;
            String username = null;

            if(provider.equals("github")) {
                URI emailUri = new URI(environment.getProperty("oauth2.github.email-url"));
                List<Object> emailList = oAuth2Api.getEmailForGithub(emailUri, prefix + " " + accessToken);

                for(Object object : emailList) {
                    Map<String, Object> result = objectMapper.convertValue(object, Map.class);

                    if((Boolean) result.get("primary")) {
                        email = result.get("email").toString();
                        break;
                    }
                }

                username = (String) userInfoMap.get("login");
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(oAuth2MemberService.loadMember(provider, email, username), null, null);

            loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        } catch (AuthenticationException e) {
            loginFailureHandler.onAuthenticationFailure(request, response, e);
        } catch (Exception e) {
            loginFailureHandler.onAuthenticationFailure(request, response, new AccountExpiredException(""));
        }
    }
}