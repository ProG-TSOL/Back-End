package com.backend.prog.domain.sample.application;

import com.backend.prog.global.S3.S3Uploader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class SampleService {

    private final S3Uploader s3Uploader;

    @Transactional
    public String saveS3Upload(MultipartFile file){
        String imgUrl = null;
        if(file != null){
            imgUrl = s3Uploader.saveUploadFile(file);
            imgUrl = s3Uploader.getFilePath(imgUrl);
        }
        return imgUrl;
    }
    /**
     * 주로 Service 클래스들 존재,
     * 주된 비즈니스 로직이 존재하는 곳
     * handler와 같은 클래스들도 포함
     */
}
