package com.backend.prog.domain.project.api;

import com.backend.prog.domain.project.application.*;
import com.backend.prog.domain.project.domain.Additional;
import com.backend.prog.domain.project.domain.ApplicationStatus;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.project.dto.AdditionalDto;
import com.backend.prog.domain.project.dto.ProjectDto;
import com.backend.prog.domain.project.dto.ProjectMemberDto;
import com.backend.prog.global.common.CommonApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final ProjectMemberService projectMemberService;
    private final ApplicationStatusService applicationStatusService;

    @PostMapping("{member-id}")
    public CommonApiResponse<?> createProject(
            @PathVariable("member-id") Integer memberId,
            @RequestPart("post") ProjectDto.Post post,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        Project result = projectService.createProject(memberId, post, file);

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
                                            @PageableDefault(page = 0, size = 20) Pageable pageable) {

        Page<ProjectDto.SimpleResponse> response = projectService.getProjects(keyword, techCodes, statusCode, sort, pageable);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(response)
                .cnt(response.getContent().size())
                .build();
    }

    @GetMapping("{project-id}/{member-id}")
    public CommonApiResponse<?> getProject(@PathVariable("project-id") Long projectId,
                                           @PathVariable("member-id") Integer memberId) {
        ProjectDto.Response response = projectService.getProject(projectId, memberId);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(response)
                .cnt(1)
                .build();
    }

    @PatchMapping("{project-id}/{member-id}")
    public CommonApiResponse<?> updateProject(@PathVariable("project-id") Long id,
                                              @PathVariable("member-id") Integer memberId,
                                              @RequestPart("patch") ProjectDto.Patch patch,
                                              @RequestParam(value = "file", required = false) MultipartFile file) {
        ProjectDto.Response response = projectService.updateProject(id, patch, file, memberId);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(response)
                .cnt(1)
                .build();
    }

    @DeleteMapping("{project-id}/{member-id}")
    public CommonApiResponse<?> deleteProject(@PathVariable("project-id") Long id,
                                              @PathVariable("member-id") Integer memberId) {

        Project response = projectService.deleteProject(id, memberId);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(response.getId())
                .cnt(1)
                .build();
    }

    @DeleteMapping("{project-id}/{member-id}/projectTotal/{job-code}")
    public CommonApiResponse<?> deleteProjectTotal(@PathVariable("project-id") Long projectId,
                                                   @PathVariable("member-id") Integer memberId,
                                                   @PathVariable("job-code") Integer jobCode) {

        projectTotalService.deleteProject(projectId, memberId, jobCode);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .cnt(1)
                .build();
    }

    @DeleteMapping("{project-id}/{member-id}/projectTech/{tech-code}")
    public CommonApiResponse<?> deleteProjectTech(@PathVariable("project-id") Long projectId,
                                                  @PathVariable("member-id") Integer memberId,
                                                  @PathVariable("tech-code") Integer techCode) {

        projectTechCodeService.deleteProject(projectId, memberId, techCode);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .cnt(1)
                .build();
    }

    @PostMapping("{project-id}/additional/{member-id}")
    public CommonApiResponse<?> createAdditional(@PathVariable("project-id") Long projectId,
                                                 @PathVariable("member-id") Integer memberId,
                                                 @RequestPart("post") AdditionalDto.Post post,
                                                 @RequestParam(value = "file", required = false) MultipartFile file) {
        Additional additional = additionalService.createAdditional(projectId, memberId, file, post);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(additional.getId())
                .cnt(1)
                .build();
    }

    @GetMapping("{project-id}/additional/{member-id}")
    public CommonApiResponse<?> getAdditional(@PathVariable("project-id") Long projectId,
                                              @PathVariable("member-id") Integer memberId) {

        List<AdditionalDto.Response> responses = additionalService.getAdditionals(projectId, memberId);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(responses)
                .cnt(responses.size())
                .build();
    }

    @PatchMapping("{additional-id}/additional/{member-id}")
    public CommonApiResponse<?> updateAdditional(@PathVariable("additional-id") Long additionalId,
                                                 @PathVariable("member-id") Integer memberId,
                                                 @RequestPart("patch") AdditionalDto.Patch patch,
                                                 @RequestParam(value = "file", required = false) MultipartFile file) {

        AdditionalDto.Response responses = additionalService.updateAdditional(additionalId, memberId, patch, file);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(responses)
                .cnt(1)
                .build();
    }

    @DeleteMapping("{additional-id}/additional/{member-id}")
    public CommonApiResponse<?> deleteAdditional(@PathVariable("additional-id") Long additionalId,
                                                 @PathVariable("member-id") Integer memberId) {

        Additional responses = additionalService.deleteAdditional(additionalId, memberId);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(responses.getId())
                .cnt(1)
                .build();
    }

    @PatchMapping("{project-id}/start/{member-id}")
    public CommonApiResponse<?> startProject(@PathVariable("project-id") Long projectId,
                                             @PathVariable("member-id") Integer memberId) {
        Project project = projectService.startProject(projectId, memberId);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(project.getId())
                .build();
    }

    @PatchMapping("{project-id}/end/{member-id}")
    public CommonApiResponse<?> endProject(@PathVariable("project-id") Long projectId,
                                           @PathVariable("member-id") Integer memberId) {
        Project project = projectService.endProject(projectId, memberId);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(project.getId())
                .cnt(1)
                .build();
    }

    @PostMapping("{project-id}/application/{member-id}")
    public CommonApiResponse<?> createApplication(@PathVariable("project-id") Long projectId,
                                                  @PathVariable("member-id") Integer memberId,
                                                  @RequestParam("jobCode") Integer jobCode) {

        applicationStatusService.createApplicationStatus(projectId, memberId, jobCode);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .cnt(1)
                .build();
    }

    @PostMapping("{project-id}/acceptMember/{member-id}")
    public CommonApiResponse<?> acceptMember(@PathVariable("project-id") Long projectId,
                                             @PathVariable("member-id") Integer memberId,
                                             @RequestParam("acceptMemberId") Integer acceptMemberId){
        applicationStatusService.acceptMember(projectId, memberId, acceptMemberId);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .cnt(1)
                .build();
    }

    @DeleteMapping("{project-id}/refuseMember/{member-id}")
    public CommonApiResponse<?> refuseMember(@PathVariable("project-id") Long projectId,
                                             @PathVariable("member-id") Integer memberId,
                                             @RequestParam("refusememberId") Integer acceptMemberId){
        applicationStatusService.refuseMember(projectId, memberId, acceptMemberId);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .cnt(1)
                .build();
    }

    @GetMapping("{project-id}/embers")
    public CommonApiResponse<?> getProjectMembers(@PathVariable("project-id") Long projecId){

        List<ProjectMemberDto.Response> responses = projectMemberService.getProjectMembers(projecId);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(responses)
                .cnt(1)
                .build();
    }

    @DeleteMapping("{project-id}/exiled/{member-id}")
    public CommonApiResponse<?> deleteProjectMember(@PathVariable("project-id") Long projectId,
                                                    @PathVariable("member-id") Integer memberId,
                                                    @RequestParam("exileId") Integer exiledId){

        projectMemberService.deleteProjectMember(projectId, memberId, exiledId);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .cnt(1)
                .build();
    }

    @PostMapping("{project-id}/like/{member-id}/add")
    public CommonApiResponse<?> addLike(@PathVariable("project-id") Long projectId,
                                        @PathVariable("member-id") Integer memberId){
        projectService.addLike(projectId, memberId);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .cnt(1)
                .build();
    }

    @DeleteMapping("{project-id}/like/{member-id}/delete")
    public CommonApiResponse<?> deleteLike(@PathVariable("project-id") Long projectId,
                                        @PathVariable("member-id") Integer memberId){
        projectService.deleteLike(projectId, memberId);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .cnt(1)
                .build();
    }
}