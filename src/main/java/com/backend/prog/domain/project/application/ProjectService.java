package com.backend.prog.domain.project.application;

import com.backend.prog.domain.manager.application.CodeCommonService;
import com.backend.prog.domain.manager.dao.CodeDetailRepository;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.member.application.MemberService;
import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.project.dao.*;
import com.backend.prog.domain.project.domain.*;
import com.backend.prog.domain.project.dto.MyProjectResponse;
import com.backend.prog.domain.project.dto.ProjectDto;
import com.backend.prog.domain.project.dto.ProjectHomeMemberResponse;
import com.backend.prog.domain.project.dto.ProjectHomeResponse;
import com.backend.prog.domain.project.mapper.ProjectMapper;
import com.backend.prog.domain.work.dao.WorkRepository;
import com.backend.prog.global.S3.S3Uploader;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final S3Uploader s3Uploader;
    private final ProjectRespository projectRespository;
    private final CodeDetailRepository codeDetailRepository;
    private final ProjectTotalService projectTotalService;
    private final ProjectTechCodeService projectTechCodeService;
    private final ProjectMemberService projectMemberService; //지우고 service 거치는거 레포로 변경해야됨
    private final ProjectMemberRespository projectMemberRespository;
    private final ProjectMapper projectMapper;
    private final MemberService memberService; //지우고 service 거치는거 레포로 변경해야됨
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final ApplicationStatusRepository applicationStatusRepository;
    private final ProjectTotalRespository projectTotalRespository;

    private final WorkRepository workRepository;
    private final CodeCommonService codeCommonService;

    private static final String BASIC_PROJECT_IMAGE_URL = "https://ssafy-prog-bucket.s3.amazonaws.com/istockphoto-1413922045-612x612.jpg";

    @Transactional
    public Project createProject(Integer memberId, ProjectDto.Post projectDto, MultipartFile file) {
        String projectImgUrl = BASIC_PROJECT_IMAGE_URL;

        if (file != null) {
            projectImgUrl = s3Uploader.saveUploadFile(file);
            projectImgUrl = s3Uploader.getFilePath(projectImgUrl);
        }

        //프로젝트 상태 코드 (프로젝트 생성시 모집중을 기본값으로 입력)
        CodeDetail statusCode = codeDetailRepository.findById(50).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        Project project = new Project(projectDto, projectImgUrl, statusCode);

        projectRespository.save(project);

        List<ProjectTechCode> projectTechCodes = new ArrayList<>();
        //기술스택 리스트
        projectDto.totechList().forEach(tech -> {
            CodeDetail techCode = codeDetailRepository.findById(tech.techCode()).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

            ProjectTechCode projectTechCode = new ProjectTechCode(project, techCode);

            projectTechCodes.add(projectTechCode);
        });

        projectTechCodeService.createProjectTechCodeList(projectTechCodes);

        // 모집인원 리스트 저장
        List<ProjectTotal> projectTotals = new ArrayList<>();

        projectDto.totalList().forEach(projectTotalPost -> {
            CodeDetail detail = codeDetailRepository.findById(
                    projectTotalPost.jobCode()).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
            ProjectTotal projectTotal = new ProjectTotal(project, detail,
                    projectTotalPost.total(), projectTotalPost.current());

            //팀장 포지션 저장
            if (projectTotalPost.current() > 0) {
                //팀장 코드 가져오기
                CodeDetail plCode = codeDetailRepository.findById(48).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
                Member pl = memberService.getMember(memberId);
                ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), memberId);
                ProjectMember projectMember = new ProjectMember(projectMemberId, project, pl, detail, plCode);

                projectMemberService.createProjectMember(projectMember);
            }
            projectTotals.add(projectTotal);
        });

        projectTotalService.createProjectTotalList(projectTotals);

        return project;
    }

    @Transactional
    public Page<ProjectDto.SimpleResponse> getProjects(Integer memberId, String keyword, Integer techCodes, Integer statusCode, Pageable pageable) {
        Page<Project> projects = projectRespository.getProject(keyword, techCodes, statusCode,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending()));
        Page<ProjectDto.SimpleResponse> result = projectMapper.entityToSimpleResponse(projects, memberId);

        return result;
    }

    @Transactional
    public ProjectDto.Response getProject(Long id, Integer memberId) {
        Project project = projectRespository.findById(id).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        project.addView();

        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), memberId);

        Optional<ProjectMember> projectMember = projectMemberService.getProjectMember(projectMemberId);

        //프로젝트 멤버가 있으면 이미 프로젝트에 존재하는 유저
        if (projectMember.isPresent()) {
            return projectMapper.entityToResponse(project, memberId, true, null);
        }

        Optional<ApplicationStatus> applicationStatus = applicationStatusRepository.findById(projectMemberId);

        if (applicationStatus.isPresent()) {
            return projectMapper.entityToResponse(project, memberId, false, applicationStatus.get());
        }

        return projectMapper.entityToResponse(project, memberId, false, null);
    }

    @Transactional
    public ProjectDto.Response updateProject(Long id, ProjectDto.Patch patch, MultipartFile file, Integer memberId) {
        Project project = projectRespository.findById(id).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        //삭제된 프로젝트면 에러
        if (project.isDeleted()) {
            throw new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST);
        }

        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), memberId);

        ProjectMember projectMember = projectMemberRespository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        //요청한 멤버가 팀장인지 검사
        if (projectMember.getRoleCode().getId() != 48) {
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }

        String projectImgUrl = "";

        if (file != null) {
            projectImgUrl = s3Uploader.saveUploadFile(file);
            projectImgUrl = s3Uploader.getFilePath(projectImgUrl);
        }

        project.updateProject(patch, projectImgUrl);
        // 프로젝트 기슬스택 수정
        // 기술스택 코드 디테일 조회해서 프로젝트에 추가(이미있는 값이면 addTechCode에서 쳐냄
        patch.totechList().forEach(tech -> {
            CodeDetail techCode = codeDetailRepository.findById(tech.techCode()).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

            ProjectTechCode projectTechCode = new ProjectTechCode(project, techCode);

            if (!project.hasTechCode(projectTechCode)) {
                projectTechCodeService.updateProjectTechCode(projectTechCode);
                project.addTech(projectTechCode);
            }
        });

        //프로젝트 포지션별 총원 저장
        //프로젝트 토탈 테이블 조회해서 이미 있는 값이면 총원, 현재원 값만 수정
        //새로운 값이면 addTotal로 추가
        patch.totalList().forEach(total -> {
            ProjectCodeDetaliId ptId = new ProjectCodeDetaliId(project.getId(), total.jobCode());

            Optional<ProjectTotal> findTotal = projectTotalService.getTotal(ptId);
            //이미 존재하는 포지션 -> total, current만 수정해서 save
            if (findTotal.isPresent()) {
                ProjectTotal projectTotal = findTotal.get();
                if (projectTotal.getTotal() < projectTotal.getCurrent()) {
                    throw new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST);
                }
                projectTotal.setTotal(total.total());
                projectTotalService.updateProjectTotal(projectTotal);
            } else { // 새로운 포지션 ProjectTotal 만들어서 save
                CodeDetail detail = codeDetailRepository.findById(total.jobCode()).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
                ProjectTotal projectTotal = new ProjectTotal(project, detail, total.total(), total.current());
                projectTotalService.updateProjectTotal(projectTotal);
                project.addTotal(projectTotal);
            }
        });

        projectRespository.save(project);

        return projectMapper.entityToResponse(project, memberId, true, null);
    }

    public Project deleteProject(Long projectId, Integer memberId) {
        Project project = projectRespository.findById(projectId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), memberId);

        ProjectMember projectMember = projectMemberRespository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        //요청한 멤버가 팀장인지 검사
        if (projectMember.getRoleCode().getId() != 48) {
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }
        project.setDeleted(true);

        projectRespository.save(project);

        return project;
    }

    @Transactional
    public Project startProject(Long projectId, Integer memberId) {
        // 멤버가 팀장인지 확인
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        Optional<ProjectMember> projectMember = projectMemberRespository.findById(projectMemberId);

        if (!projectMember.isPresent() || !projectMember.get().getRoleCode().getId().equals(48)) {
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }

        Project project = projectRespository.findById(projectId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        //이미 시작된 프로젝트인지 확인
        if (project.getStartDay() == null) {
            CodeDetail start = codeDetailRepository.findById(51).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
            project.startProject(start);
        } else {
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }

        Project saveProject = projectRespository.save(project);

//        종료일 계산 =>
        Integer period = saveProject.getPeriod();
        LocalDate startDay = saveProject.getStartDay();
        LocalDate endDate = startDay.plusDays(period * 7L);
        saveProject.updateEndDate(endDate);

        return project;
    }

    public Project endProject(Long projectId, Integer memberId) {
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        Optional<ProjectMember> projectMember = projectMemberRespository.findById(projectMemberId);

        if (!projectMember.isPresent() || projectMember.get().getRoleCode().getId() != 48) {
            throw new CommonException(ExceptionEnum.ALREADY_EXIST_START);
        }

        Project project = projectRespository.findById(projectId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        //이미 시작된 프로젝트인지 확인
        if (project.getStartDay() != null || project.getEndDay() == null) {
            CodeDetail end = codeDetailRepository.findById(52).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
            project.endProject(end);
        } else {
            throw new CommonException(ExceptionEnum.ALREADY_EXIST_END);
        }

        projectRespository.save(project);

        return project;
    }

    @Transactional
    public void addLike(Long projectId, Integer memberId) {
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        Optional<Like> findLike = likeRepository.findById(projectMemberId);

        //이미 좋아요를 누른 멤버인지 검사
        if (findLike.isPresent()) {
            throw new CommonException(ExceptionEnum.ALREADY_EXIST_Like);
        }

        Project project = projectRespository.findById(projectId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        Like like = new Like(projectMemberId, project, member);

        //프로젝트 좋아요 수 +1
        project.addLike();

        projectRespository.save(project);

        likeRepository.save(like);
    }

    @Transactional
    public void deleteLike(Long projectId, Integer memberId) {
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        Like findLike = likeRepository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        Project project = projectRespository.findById(projectId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        //프로젝트 좋아요 수 -1
        project.deleteLike();

        projectRespository.save(project);

        likeRepository.delete(findLike);
    }

    /**
     * 내가 신청, 참여중인 프로젝트 목록
     */
    public List<MyProjectResponse> getMyProjects(Integer memberId) {
        List<Project> list = projectRespository.findAllMyProject(memberId);
        return list.stream()
                .map(project -> {
                    Integer total = getTotalParticipationProject(project.getId());
                    Integer progress = getProgress(project.getStartDay(), project.getEndDay());
                    return new MyProjectResponse().toDto(project, total, progress);
                })
                .toList();
    }

    /**
     * 참여중인 프로젝트를 상태코드별로 조회
     */
    public List<MyProjectResponse> getMyProjects(Integer memberId, Integer statusCode) {
        List<Project> list = projectRespository.findMyProjectByStatus(memberId, statusCode);
        return list.stream()
                .map(project -> {
                    Integer total = getTotalParticipationProject(project.getId());
                    Integer progress = getProgress(project.getStartDay(), project.getEndDay());
                    return new MyProjectResponse().toDto(project, total, progress);
                })
                .toList();
    }

    /**
     * 내가 신청중인 프로젝트 목록
     */
    public List<MyProjectResponse> getSignedMyProjects(Integer memberId) {
        List<Project> list = projectRespository.findAllMySignedProject(memberId);
        return list.stream()
                .map(project -> {
                    Integer total = getTotalParticipationProject(project.getId());
                    Integer progress = getProgress(project.getStartDay(), project.getEndDay());
                    return new MyProjectResponse().toDto(project, total, progress);
                })
                .toList();
    }

    public List<ProjectDto.HotResponse> getHotProject() {

        List<Project> list = projectRespository.findHotProject();

        return projectMapper.entityToHotResponse(list);
    }

    //    Current number of people participating in the project
    public Integer getTotalParticipationProject(Long projectId) {
        Integer result = projectTotalRespository.countCurrentByProjectId(projectId);
        return result == null ? 0 : result;
    }

    public ProjectHomeResponse getProjectHome(Long projectId, Integer memberId) {
        Project project = projectRespository.findById(projectId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        Integer countMyWork = workRepository.findCountMyWork(projectId, memberId);
        Integer progress = getProgress(project.getStartDay(), project.getEndDay());
        return new ProjectHomeResponse().toDto(project, progress, countMyWork);
    }

    public List<ProjectHomeMemberResponse> getProjectHomeMember(Long projectId) {
        List<ProjectMember> projectMemberList = projectMemberRespository.findAllByProjectId(projectId);
        return projectMemberList.stream()
                .map(projectmember -> {
                    CodeDetail jobCode = codeCommonService.getCodeDetail(projectmember.getJobCode().getId());
                    CodeDetail roleCode = codeCommonService.getCodeDetail(projectmember.getRoleCode().getId());
                    return new ProjectHomeMemberResponse().toDto(projectmember.getMember(), jobCode, roleCode);
                }).toList();
    }

    /**
     * 프로젝트 진행률 계산
     */
    public Integer getProgress(LocalDate startDay, LocalDate endDay) {
        if (startDay == null || endDay == null) return 0;
        if (startDay.equals(endDay)) return 100;

        LocalDate now = LocalDate.now();
        long totalDays = ChronoUnit.DAYS.between(startDay, endDay);
        long elapsedDays = ChronoUnit.DAYS.between(startDay, now);

        int result = (int) Math.round((double) elapsedDays / totalDays * 100);
        return Math.min(Math.max(result, 0), 100);
    }
}
