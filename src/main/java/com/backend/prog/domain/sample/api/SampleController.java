package com.backend.prog.domain.sample.api;

import com.backend.prog.domain.sample.application.SampleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
public class SampleController {

    private final SampleService sampleService;

    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @GetMapping("/test/s3upload")
    public String test (@RequestParam(value = "image", required = false) MultipartFile file) {
        String imgUrl = sampleService.saveS3Upload(file);
        log.info(imgUrl + "S3 Upload Success");
        return imgUrl;
    }

    /**
     * SampleController 보통은 controller 클래스 생성
     */
}
