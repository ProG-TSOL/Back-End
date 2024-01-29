package com.backend.prog.global.common;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collection;

@RestControllerAdvice(
        basePackages = "com.backend.prog.domain" // 다른 open API 사용시에는 공통응답으로 처리하지 않음, 직접 개발한 도메인에 대해서만 처리
)
public class ResponseWrapper implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 컨트롤러의 반환 타입이 객체일때 직렬화를 위해 -> MappingJackson2HttpMessageConverter 사용
        // 해방 컨버터가 사용될 경우에만 ResponseBodyAdvice가 실행되도록 설정
        return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
//        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        int cnt = 0;
        cnt = getDataCnt(body);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(body)
                .cnt(cnt)
                .build();
    }

    /**
     * 데이터의 개수를 구하는 메소드
     * @return 데이터의 개수
     */
    private static int getDataCnt(Object body) {
        int cnt;
        if (body instanceof Collection) {
            cnt =  ((Collection<?>) body).size();
        } else if (body instanceof  Object[]) {
            cnt = ((Object[]) body).length;
        } else {
            cnt = 1;
        }
        return cnt;
    }
}
