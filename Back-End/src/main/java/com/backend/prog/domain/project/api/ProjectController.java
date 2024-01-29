package com.backend.prog.domain.project.api;

import com.backend.prog.domain.manager.dao.CodeDetailRepository;
import com.backend.prog.domain.manager.dao.CodeRepository;
import com.backend.prog.domain.manager.domain.Code;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.project.application.AdditionalService;
import com.backend.prog.domain.project.application.ProjectService;
import com.backend.prog.domain.project.application.ProjectTechCodeService;
import com.backend.prog.domain.project.application.ProjectTotalService;
import com.backend.prog.domain.project.domain.Additional;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.project.dto.AdditionalDto;
import com.backend.prog.domain.project.dto.ProjectDto;
import com.backend.prog.domain.project.dto.ProjectDtoSample;
import com.backend.prog.global.common.CommonApiResponse;
import com.backend.prog.global.common.ResponseWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;


@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectTotalService projectTotalService;
    private final ProjectTechCodeService projectTechCodeService;
    private final AdditionalService additionalService;

    @PostMapping
    public CommonApiResponse<?> createProject(
//            @AuthenticationPrincipal MemberDto.Post member,
            @RequestPart("post") ProjectDto.Post post,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        Project result = projectService.createProject(post, file);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(result.getId())
                .cnt(1)
                .build();
    }

    @GetMapping
    public CommonApiResponse<?> getProjects(@RequestParam(name = "keyword", required = false) String keyword,
                                            @RequestParam(name = "techCodes", required = false) Integer techCodes,
                                            @RequestParam(name = "statusCode", required = false) Integer statusCode,
                                            @RequestParam(name = "sort", required = false) String sort,
                                            @PageableDefault(page = 0, size = 20) Pageable pageable){

        Page<ProjectDto.SimpleResponse> response = projectService.getProjects(keyword, techCodes, statusCode, sort, pageable);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(response)
                .cnt(response.getSize())
                .build();
    }

    @GetMapping("{project-id}")
    public CommonApiResponse<?> getProject(@PathVariable("project-id") Long id){
        ProjectDto.Response response = projectService.getProject(id);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(response)
                .cnt(1)
                .build();
    }

    @PatchMapping("{project-id}")
    public CommonApiResponse<?> updateProject(@PathVariable("project-id") Long id,
                                              @RequestPart("patch") ProjectDto.Patch patch,
                                              @RequestParam(value = "file", required = false) MultipartFile file){
        ProjectDto.Response response = projectService.updateProject(id, patch, file);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(response)
                .cnt(1)
                .build();
    }

    @DeleteMapping("{project-id}")
    public CommonApiResponse<?> deleteProject(@PathVariable("project-id") Long id){

        Project response = projectService.deleteProject(id);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(response.getId())
                .cnt(1)
                .build();
    }

    @DeleteMapping("{project-id}/projectTotal/{job-code}")
    public CommonApiResponse<?> deleteProjectTotal(@PathVariable("project-id") Long projectId,
                                              @PathVariable("job-code") Integer jobCode){
        log.info("{}", projectId);
        projectTotalService.deleteProject(projectId, jobCode);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(1)
                .cnt(1)
                .build();
    }

    @DeleteMapping("{project-id}/projectTech/{tech-code}")
    public CommonApiResponse<?> deleteProjectTech(@PathVariable("project-id") Long projectId,
                                                  @PathVariable("tech-code") Integer techCode){

        projectTechCodeService.deleteProject(projectId, techCode);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(1)
                .cnt(1)
                .build();
    }

    @PostMapping("{project-id}/additional")
    public CommonApiResponse<?> createAdditional(@PathVariable("project-id") Long projectId,
                                                 @RequestPart("post")AdditionalDto.Post post,
                                                 @RequestParam(value = "file", required = false) MultipartFile file){
        Additional additional = additionalService.createAdditional(projectId, file, post);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(additional.getId())
                .cnt(1)
                .build();
    }

    @GetMapping("{project-id}/additional")
    public CommonApiResponse<?> getAdditional(@PathVariable("project-id") Long projectId){

        List<AdditionalDto.Response> responses = additionalService.getAdditionals(projectId);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(responses)
                .cnt(responses.size())
                .build();
    }
}
