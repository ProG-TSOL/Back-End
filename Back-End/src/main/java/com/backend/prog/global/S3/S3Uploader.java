package com.backend.prog.global.S3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Service
@Slf4j
public class S3Uploader {
    // S3와 상호작용 버킷 생성, 삭제, 리스트 조회, 객체 업/다운 로드, 객체 삭제 등 작업 수행하는 클래스
    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveUploadFile(MultipartFile multipartFile) {
        validateFileExits(multipartFile);
        // AWS SDK에 포함된 클래스, S3에 객체 업/다운 로드 시 사용하는 메타 데이터
        ObjectMetadata objectMetadata = new ObjectMetadata();
        //객체의 미디어 타입
        objectMetadata.setContentType(multipartFile.getContentType());
        //파일 크기 설정
        objectMetadata.setContentLength(multipartFile.getSize());

        String fileName = createFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (InputStream inputStream = multipartFile.getInputStream()) {
            //PutObjectRequest =  객체를 업로드하는 요청, inputStream = 업로드할 파일의 내용
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    // withCannedAcl = 객체 접근 권한 설정, CannedAccessControlList.PublicRead = 공개적으로 읽기 가능
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }
        return fileName;
    }

    // 파일 경로 가져오는 메서드, 해당 버킷 안에 fileName의 파일이 어디있는지 검사
    public String getFilePath(String fileName) {
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 파일 삭제
    public void deleteFile(String fileName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    //파일 이름 생성
    private String createFileName(String fileName) {
        try {
            //파일 확장자 추출 .PNG = PNG
            String ext = fileName.substring(fileName.lastIndexOf("."));
            //파일 이름 안겹치게 UUID로 랜덤값 줌
            return UUID.randomUUID() + ext;
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

    //파일 비어있는지 확인
    private void validateFileExits(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new RuntimeException("파일이 비어있습니다.");
        }
    }
}