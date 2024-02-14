package com.backend.prog.global.S3.application;

import com.backend.prog.global.S3.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Uploader s3Uploader;

    public String saveImages(MultipartFile file) {

        String projectImgUrl = "";

        if (file != null){
            projectImgUrl = s3Uploader.saveUploadFile(file);
            projectImgUrl = s3Uploader.getFilePath(projectImgUrl);
        }

        return projectImgUrl;
    }
}
