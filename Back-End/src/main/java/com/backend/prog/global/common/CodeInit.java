//package com.backend.prog.global.common;
//
//import com.backend.prog.domain.manager.dao.CodeDetailRepository;
//import com.backend.prog.domain.manager.dao.CodeRepository;
//import com.backend.prog.domain.manager.domain.Code;
//import com.backend.prog.domain.manager.domain.CodeDetail;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class CodeInit implements InitializingBean {
//    private final CodeRepository codeRepository;
//    private final CodeDetailRepository codeDetailRepository;
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        initCode();
//    }
//    public void initCode(){
//        Code code = Code.builder()
//                .name("Tech")
//                .description("테스트 코드")
//                .build();
//        codeRepository.save(code);
//
//        for (int i = 0; i < 20; i++) {
//            CodeDetail detail = CodeDetail.builder()
//                    .code(code)
//                    .detailName("Java" + i)
//                    .detailDescription(i + "번째 테스트 코드")
//                    .build();
//            codeDetailRepository.save(detail);
//        }
//    }
//}
