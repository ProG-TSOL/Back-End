package com.backend.prog.global.config;

import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.global.auth.filter.JwtAuthenticationFilter;
import com.backend.prog.global.auth.filter.JwtExceptionFilter;
import com.backend.prog.global.auth.filter.LoginAuthenticationFilter;
import com.backend.prog.global.auth.handler.*;
import com.backend.prog.global.auth.service.MemberDetailsService;
import com.backend.prog.global.util.JwtUtil;
import com.backend.prog.global.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Log4j2
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    private final ResponseUtil responseUtil;

    private final MemberDetailsService memberDetailsService;

    private final MemberRepository memberRepository;

    private final String[] ALLOWED_MEMBER_URL = {"/members/login", "/members/sign-up", "/members/nickName-validation-check/{nickname}"
            , "/members/email-verification", "/members/email-verification-confirm"
            , "/members/profile/{email}", "/members/detail-profile/{email}"};

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        http.formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(request ->
                request.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers(ALLOWED_MEMBER_URL).permitAll()
                        .anyRequest().authenticated())
                .logout(logout -> logout
                        .logoutUrl("/members/logout")
                        .logoutUrl("/members/withdrawal-member")
                        .addLogoutHandler(jwtLogoutHandler())
                        .logoutSuccessHandler(jwtLogoutSuccessHandler()));

        http.addFilterBefore(jwtExceptionFilter(), LogoutFilter.class);
        http.addFilterBefore(loginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Set-Cookie"));
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
        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter();
        loginAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        loginAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());
        loginAuthenticationFilter.setAuthenticationManager(authenticationManager());
        return loginAuthenticationFilter;
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
    public JwtLogoutHandler jwtLogoutHandler() {
        return new JwtLogoutHandler(jwtUtil, memberRepository);
    }


    @Bean
    public JwtLogoutSuccessHandler jwtLogoutSuccessHandler() {
        return new JwtLogoutSuccessHandler();
    }
}