package com.backend.prog.domain.project.application;

import com.backend.prog.domain.project.dao.AdditionalRepository;
import com.backend.prog.domain.project.dao.ProjectRespository;
import com.backend.prog.domain.project.domain.Additional;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.project.dto.AdditionalDto;
import com.backend.prog.global.S3.S3Uploader;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdditionalService {
    private final AdditionalRepository additionalRepository;
    private final ProjectRespository projectRespository;
    private final S3Uploader s3Uploader;

    public Additional createAdditional(Long projectId, MultipartFile file, AdditionalDto.Post post) {
        int sum = additionalRepository.countAllByProjectId(projectId);

        if (sum > 5){
            //5개 이상이면 오류 발생
            throw new CommonException(ExceptionEnum.DATA_CANNOT_ADD);
        }

        Project project = projectRespository.findById(projectId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        String imgUrl = "";

        if (file != null) {
            imgUrl = s3Uploader.saveUploadFile(file);
            imgUrl = s3Uploader.getFilePath(imgUrl);
        }

        Additional additional = new Additional(post, imgUrl, project);

        additionalRepository.save(additional);

        return additional;
    }

    public List<AdditionalDto.Response> getAdditionals(Long projectId) {
        List<Additional> list = additionalRepository.findAllByProjectId(projectId);

        return list.stream().map(additional -> AdditionalDto.Response.builder()
                        .id(additional.getId())
                        .title(additional.getTitle())
                        .url(additional.getUrl())
                        .imgUrl(additional.getImgUrl())
                        .build())
                .toList();
    }
}
