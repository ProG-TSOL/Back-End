package com.backend.prog.global.config;

import com.backend.prog.global.auth.service.OAuth2MemberService;
import com.backend.prog.global.auth.api.OAuth2Api;
import com.backend.prog.global.auth.filter.*;
import com.backend.prog.global.auth.handler.*;
import com.backend.prog.global.auth.service.MemberDetailsService;
import com.backend.prog.global.util.JwtUtil;
import com.backend.prog.global.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    private final ResponseUtil responseUtil;

    private final MemberDetailsService memberDetailsService;

    private final Environment environment;

    private final ObjectMapper objectMapper;

    private final OAuth2Api oAuth2Api;

    private final OAuth2MemberService oAuth2MemberService;

    private static final String[] ALLOWED_PATTERNS = {"/members/login", "/members/sign-up", "/members/nickname-validation-check/**"
            , "/members/email-verification", "/members/email-verification-confirm", "/members/profile/{email}", "/members/detail-profile/{email}"
            , "/oauth2/authorization/{providerReg}", "/login/oauth2/code/github", "/codes", "/codes/{codename}", "/codes/details/{codename}", "/codes/details/detail/{detailCodeId}"
            , "/codes", "/codes/{codename}", "/codes/details/{codename}", "/codes/details/detail/{detailCodeId}"};

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(request -> request.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers(Arrays.stream(ALLOWED_PATTERNS)
                                .map(pattern -> "/api" + pattern)
                                .toArray(String[]::new)).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/comments/**", "/api/projects/**").permitAll()
                        .anyRequest().hasRole("USER"))
                .logout(logout -> logout
                        .logoutUrl("/members/logout")
                        .addLogoutHandler(jwtLogoutHandler())
                        .logoutSuccessHandler(jwtLogoutSuccessHandler()))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint()).accessDeniedHandler(customAccessDeniedHandler()));

        http.addFilterAfter(loginAuthenticationFilter(), LogoutFilter.class);
        http.addFilterAfter(oAuth2LoginFilter(), LoginAuthenticationFilter.class);
        http.addFilterAfter(jwtExceptionFilter(), OAuth2LoginFilter.class);
        http.addFilterAfter(jwtReissueTokenFilter(), JwtExceptionFilter.class);
        http.addFilterAfter(jwtAuthenticationFilter(), JwtReissueTokenFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Cookie"));
        configuration.setExposedHeaders(Arrays.asList("Content-Type", "accessToken","Set-Cookie"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(memberDetailsService);
        provider.setHideUserNotFoundExceptions(false);
        return new ProviderManager(provider);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtUtil);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler(responseUtil);
    }

    @Bean
    public LoginAuthenticationFilter loginAuthenticationFilter() {
        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter(objectMapper);
        loginAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        loginAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());
        loginAuthenticationFilter.setAuthenticationManager(authenticationManager());
        return loginAuthenticationFilter;
    }

    @Bean
    public OAuth2LoginFilter oAuth2LoginFilter() {
        return new OAuth2LoginFilter(loginSuccessHandler(), loginFailureHandler(), environment, objectMapper, oAuth2Api, oAuth2MemberService);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        return new JwtExceptionFilter(responseUtil);
    }

    @Bean
    public JwtReissueTokenFilter jwtReissueTokenFilter() {
        return new JwtReissueTokenFilter(jwtUtil);
    }

    @Bean
    public JwtLogoutHandler jwtLogoutHandler() {
        return new JwtLogoutHandler(jwtUtil);
    }

    @Bean
    public JwtLogoutSuccessHandler jwtLogoutSuccessHandler() {
        return new JwtLogoutSuccessHandler();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint(responseUtil);
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler(responseUtil);
    }
}
