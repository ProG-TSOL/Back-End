package com.backend.prog.domain.project.api;

import com.backend.prog.domain.project.application.*;
import com.backend.prog.domain.project.domain.Additional;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.project.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectTotalService projectTotalService;
    private final ProjectTechCodeService projectTechCodeService;
    private final AdditionalService additionalService;
    private final ProjectMemberService projectMemberService;
    private final ApplicationStatusService applicationStatusService;

    @PostMapping("{member-id}")
    public void createProject(
            @PathVariable("member-id") Integer memberId,
            @RequestPart("post") ProjectDto.Post post,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        Project result = projectService.createProject(memberId, post, file);
    }

    @GetMapping
    public Object getProjects(Integer id,
            @RequestParam(name = "keyword", required = false) String keyword,
                              @RequestParam(name = "techCodes", required = false) Integer techCodes,
                              @RequestParam(name = "statusCode", required = false) Integer statusCode,
                              @PageableDefault(page = 0, size = 20) Pageable pageable) {

        Page<ProjectDto.SimpleResponse> response = projectService.getProjects(id, keyword, techCodes, statusCode, pageable);

        return response;
    }

    @GetMapping("{project-id}/{member-id}")
    public Object getProject(@PathVariable("project-id") Long projectId,
                             @PathVariable("member-id") Integer memberId) {
        ProjectDto.Response response = projectService.getProject(projectId, memberId);

        return response;
    }

    @PatchMapping("{project-id}/{member-id}")
    public void updateProject(@PathVariable("project-id") Long id,
                              @PathVariable("member-id") Integer memberId,
                              @RequestPart("patch") ProjectDto.Patch patch,
                              @RequestParam(value = "file", required = false) MultipartFile file) {
        ProjectDto.Response response = projectService.updateProject(id, patch, file, memberId);
    }

    @DeleteMapping("{project-id}/{member-id}")
    public void deleteProject(@PathVariable("project-id") Long id,
                              @PathVariable("member-id") Integer memberId) {

        Project response = projectService.deleteProject(id, memberId);
    }

    @DeleteMapping("{project-id}/{member-id}/projectTotal/{job-code}")
    public void deleteProjectTotal(@PathVariable("project-id") Long projectId,
                                   @PathVariable("member-id") Integer memberId,
                                   @PathVariable("job-code") Integer jobCode) {

        projectTotalService.deleteProject(projectId, memberId, jobCode);
    }

    @DeleteMapping("{project-id}/{member-id}/projectTech/{tech-code}")
    public void deleteProjectTech(@PathVariable("project-id") Long projectId,
                                  @PathVariable("member-id") Integer memberId,
                                  @PathVariable("tech-code") Integer techCode) {

        projectTechCodeService.deleteProject(projectId, memberId, techCode);
    }

    @PostMapping("{project-id}/additional/{member-id}")
    public void createAdditional(@PathVariable("project-id") Long projectId,
                                 @PathVariable("member-id") Integer memberId,
                                 @RequestPart("post") AdditionalDto.Post post,
                                 @RequestParam(value = "file", required = false) MultipartFile file) {
        Additional additional = additionalService.createAdditional(projectId, memberId, file, post);
    }

    @GetMapping("{project-id}/additional/{member-id}")
    public Object getAdditional(@PathVariable("project-id") Long projectId,
                                @PathVariable("member-id") Integer memberId) {

        List<AdditionalDto.Response> responses = additionalService.getAdditionals(projectId, memberId);

        return responses;
    }

    @PatchMapping("{additional-id}/additional/{member-id}")
    public void updateAdditional(@PathVariable("additional-id") Long additionalId,
                                 @PathVariable("member-id") Integer memberId,
                                 @RequestPart("patch") AdditionalDto.Patch patch,
                                 @RequestParam(value = "file", required = false) MultipartFile file) {

        AdditionalDto.Response responses = additionalService.updateAdditional(additionalId, memberId, patch, file);
    }

    @DeleteMapping("{additional-id}/additional/{member-id}")
    public void deleteAdditional(@PathVariable("additional-id") Long additionalId,
                                 @PathVariable("member-id") Integer memberId) {

        Additional responses = additionalService.deleteAdditional(additionalId, memberId);
    }

    @PatchMapping("{project-id}/start/{member-id}")
    public void startProject(@PathVariable("project-id") Long projectId,
                             @PathVariable("member-id") Integer memberId) {
        Project project = projectService.startProject(projectId, memberId);
    }

    @PatchMapping("{project-id}/end/{member-id}")
    public void endProject(@PathVariable("project-id") Long projectId,
                           @PathVariable("member-id") Integer memberId) {
        Project project = projectService.endProject(projectId, memberId);
    }

    @PostMapping("{project-id}/application/{member-id}")
    public void createApplication(@PathVariable("project-id") Long projectId,
                                  @PathVariable("member-id") Integer memberId,
                                  @RequestParam("jobCode") Integer jobCode) {

        applicationStatusService.createApplicationStatus(projectId, memberId, jobCode);
    }

    @GetMapping("{project-id}/application/{member-id}")
    public Object getApplications(@PathVariable("project-id") Long projectId,
                                 @PathVariable("member-id") Integer memberId) {

        List<ApplicationStatusDto.Response> responses = applicationStatusService.getApplications(projectId, memberId);

        return responses;
    }

    @PostMapping("{project-id}/acceptMember/{member-id}")
    public void acceptMember(@PathVariable("project-id") Long projectId,
                             @PathVariable("member-id") Integer memberId,
                             @RequestParam("acceptMemberId") Integer acceptMemberId) {
        applicationStatusService.acceptMember(projectId, memberId, acceptMemberId);
    }

    @DeleteMapping("{project-id}/refuseMember/{member-id}")
    public void refuseMember(@PathVariable("project-id") Long projectId,
                             @PathVariable("member-id") Integer memberId,
                             @RequestParam("refuseMemberId") Integer acceptMemberId) {
        applicationStatusService.refuseMember(projectId, memberId, acceptMemberId);
    }

    @GetMapping("{project-id}/members")
    public Object getProjectMembers(@PathVariable("project-id") Long projecId) {

        List<ProjectMemberDto.Response> responses = projectMemberService.getProjectMembers(projecId);

        return responses;
    }

    @DeleteMapping("{project-id}/exiled/{member-id}")
    public void deleteProjectMember(@PathVariable("project-id") Long projectId,
                                    @PathVariable("member-id") Integer memberId,
                                    @RequestParam("exileId") Integer exiledId) {

        projectMemberService.deleteProjectMember(projectId, memberId, exiledId);
    }

    @PostMapping("{project-id}/like/{member-id}/add")
    public void addLike(@PathVariable("project-id") Long projectId,
                        @PathVariable("member-id") Integer memberId) {
        projectService.addLike(projectId, memberId);
    }

    @DeleteMapping("{project-id}/like/{member-id}/delete")
    public void deleteLike(@PathVariable("project-id") Long projectId,
                           @PathVariable("member-id") Integer memberId) {
        projectService.deleteLike(projectId, memberId);
    }
    @GetMapping("myproject/{member-id}")
    public List<MyProjectResponse> getMyProjects(@PathVariable("member-id") Integer memberId){
        return projectService.getMyProjects(memberId);
    }
    @GetMapping("myproject/{member-id}/{status-code}")
    public List<MyProjectResponse> getMyProjects(@PathVariable("member-id") Integer memberId, @PathVariable("status-code") Integer statusCode){
        return projectService.getMyProjects(memberId, statusCode);
    }
    @GetMapping("myproject-signed/{member-id}")
    public List<MyProjectResponse> getSignedMyProjects(@PathVariable("member-id") Integer memberId){
        return projectService.getSignedMyProjects(memberId);
    }

    @PatchMapping("{project-id}/{member-id}/members")
    public void updateProjectMember(@PathVariable("project-id") Long projectId,
                                    @PathVariable("member-id") Integer memberId,
                                    @RequestParam("moveMember") Integer moveMember,
                                    @RequestParam("moveJob") Integer moveJob){
        projectMemberService.updateProjectMemver(projectId, memberId, moveMember, moveJob);
    }

    @GetMapping("hotProject")
    public Object getHotProject(){
        return projectService.getHotProject();
    }

    @GetMapping("home/{projectId}/{memberId}")
    public ProjectHomeResponse getProjectHome(@PathVariable("projectId") Long projectId, @PathVariable("memberId") Integer memberId){
        return projectService.getProjectHome(projectId, memberId);
    }

    @GetMapping("home/project-member/{projectId}")
    public List<ProjectHomeMemberResponse> getProjectHomeMember(@PathVariable("projectId") Long projectId){
        return projectService.getProjectHomeMember(projectId);
    }

}