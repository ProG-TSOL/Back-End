package com.backend.prog.global.S3.api;

import com.backend.prog.global.S3.application.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping
    public String SaveImage(@RequestParam(value = "file", required = false) MultipartFile file) {
        log.info(file);
        String imgUrl = s3Service.saveImages(file);
        log.info(imgUrl);
        return imgUrl;
    }
}
