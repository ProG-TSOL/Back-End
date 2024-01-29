package com.backend.prog.global.auth.service;

import com.backend.prog.global.error.CommonExceptionDto;
import com.backend.prog.global.error.ExceptionEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ResponseService {

    public void setErrorResponse(HttpServletResponse response, ExceptionEnum exceptionEnum) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CommonExceptionDto commonExceptionDto = new CommonExceptionDto(exceptionEnum.getCode(), exceptionEnum.getMessage());
        Map<String, Object> map = new LinkedHashMap<>();

        response.setCharacterEncoding("UTF-8");
        response.setStatus(exceptionEnum.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        map.put("status", "FAIL");
        map.put("message", exceptionEnum.getMessage());
        map.put("exceptionDto", commonExceptionDto);

        response.getWriter().write(objectMapper.writeValueAsString(map));
    }

}
