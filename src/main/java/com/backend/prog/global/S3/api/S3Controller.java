package com.backend.prog.global.S3.api;

import com.backend.prog.global.S3.application.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping
    public String SaveImage(@RequestParam(value = "file", required = false) MultipartFile file) {
        String imgUrl = s3Service.saveImages(file);
        return imgUrl;
    }
}
