package com.backend.prog.domain.project.application;

import com.backend.prog.domain.project.dao.AdditionalRepository;
import com.backend.prog.domain.project.dao.ProjectMemberRespository;
import com.backend.prog.domain.project.dao.ProjectRespository;
import com.backend.prog.domain.project.domain.Additional;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.project.domain.ProjectMember;
import com.backend.prog.domain.project.domain.ProjectMemberId;
import com.backend.prog.domain.project.dto.AdditionalDto;
import com.backend.prog.global.S3.S3Uploader;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdditionalService {
    private final AdditionalRepository additionalRepository;
    private final ProjectRespository projectRespository;
    private final ProjectMemberRespository projectMemberRespository;
    private final S3Uploader s3Uploader;

    @Transactional
    public Additional createAdditional(Long projectId, Integer memberId, MultipartFile file, AdditionalDto.Post post) {
        
        // 프로젝트에 포함된 유저인지 검사
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        ProjectMember projectMember = projectMemberRespository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE));
        
        int sum = additionalRepository.countAllByProjectIdAndIdDeleteFalse(projectId);

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

    @Transactional(readOnly = true)
    public List<AdditionalDto.Response> getAdditionals(Long projectId, Integer memberId) {
        // 프로젝트에 포함된 유저인지 검사
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        ProjectMember projectMember = projectMemberRespository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE));

        List<Additional> list = additionalRepository.findAllByProjectIdAndIsDeletedFalse(projectId);

        return list.stream().map(additional -> AdditionalDto.Response.builder()
                        .id(additional.getId())
                        .title(additional.getTitle())
                        .url(additional.getUrl())
                        .imgUrl(additional.getImgUrl())
                        .build())
                .toList();
    }

    @Transactional
    public AdditionalDto.Response updateAdditional(Long additionalId, Integer memberId, AdditionalDto.Patch patch, MultipartFile file) {
        Additional additional = additionalRepository.findById(additionalId).orElseThrow(()->new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        // 프로젝트에 포함된 유저인지 검사
        ProjectMemberId projectMemberId = new ProjectMemberId(additional.getProject().getId(), memberId);
        ProjectMember projectMember = projectMemberRespository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE));


        String imgUrl = "";
        if (file != null) {
            imgUrl = s3Uploader.saveUploadFile(file);
            imgUrl = s3Uploader.getFilePath(imgUrl);
        }

        additional.updateAdditional(patch, imgUrl);

        additionalRepository.save(additional);

        return AdditionalDto.Response.builder()
                .id(additional.getId())
                .title(additional.getTitle())
                .url(additional.getUrl())
                .imgUrl(additional.getImgUrl())
                .build();
    }


    public Additional deleteAdditional(Long additionalId, Integer memberId) {
        Additional additional = additionalRepository.findById(additionalId).orElseThrow(()->new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        // 프로젝트에 포함된 유저인지 검사
        ProjectMemberId projectMemberId = new ProjectMemberId(additional.getProject().getId(), memberId);
        ProjectMember projectMember = projectMemberRespository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE));

        additional.setDeleted(true);

        additionalRepository.save(additional);

        return additional;
    }
}
