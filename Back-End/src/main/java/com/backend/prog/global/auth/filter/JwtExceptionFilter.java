package com.backend.prog.global.auth.filter;

import com.backend.prog.global.auth.service.ResponseService;
import com.backend.prog.global.error.CommonException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ResponseService responseService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }  catch (CommonException e) {
            responseService.setErrorResponse(response, e.getError());
        }
    }
}
