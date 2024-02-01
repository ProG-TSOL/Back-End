package com.backend.prog.domain.project.application;

import com.backend.prog.domain.manager.dao.CodeDetailRepository;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.member.application.MemberService;
import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.project.dao.LikeRepository;
import com.backend.prog.domain.project.dao.ProjectMemberRespository;
import com.backend.prog.domain.project.dao.ProjectRespository;
import com.backend.prog.domain.project.domain.*;
import com.backend.prog.domain.project.dto.ProjectDto;
import com.backend.prog.domain.project.mapper.ProjectMapper;
import com.backend.prog.global.S3.S3Uploader;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
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

    @Transactional
    public Project createProject(Integer memberId, ProjectDto.Post projectDto, MultipartFile file) {
        String projectImgUrl = "";

        if (file != null){
            projectImgUrl = s3Uploader.saveUploadFile(file);
            projectImgUrl = s3Uploader.getFilePath(projectImgUrl);
        }

        //프로젝트 상태 코드 (프로젝트 생성시 모집중을 기본값으로 입력)
        CodeDetail statusCode = codeDetailRepository.findById(19).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

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
            if(projectTotalPost.current() > 0){
                //팀장 코드 가져오기
                CodeDetail plCode =  codeDetailRepository.findById(17).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
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
    public Page<ProjectDto.SimpleResponse> getProjects(String keyword, Integer techCodes, Integer statusCode, String sort, Pageable pageable) {
        CodeDetail tech = null;

        if(techCodes != null){
             tech = codeDetailRepository.findById(techCodes).orElseThrow(() ->new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        }

        Page<Project> projects = projectRespository.getProject(keyword, tech, statusCode, PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by("createdAt").descending()));

        Page<ProjectDto.SimpleResponse> result = projectMapper.entityToSimpleResponse(projects);

        return result;
    }

    public ProjectDto.Response getProject(Long id, Integer memberId) {
        Project project = projectRespository.findById(id).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), memberId);

        Optional<ProjectMember> projectMember = projectMemberService.getProjectMember(projectMemberId);

        //프로젝트 멤버가 있으면 이미 프로젝트에 존재하는 유저
        if (projectMember.isPresent()){
            return projectMapper.entityToResponse(project, true);
        }else {
            return projectMapper.entityToResponse(project, false);
        }
    }

    @Transactional
    public ProjectDto.Response updateProject(Long id, ProjectDto.Patch patch, MultipartFile file, Integer memberId) {
        Project project = projectRespository.findById(id).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), memberId);

        ProjectMember projectMember = projectMemberRespository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        
        //요청한 멤버가 팀장인지 검사
        if(projectMember.getRoleCode().getId() != 17){
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }

        String projectImgUrl = "";

        if (file != null){
            projectImgUrl = s3Uploader.saveUploadFile(file);
            projectImgUrl = s3Uploader.getFilePath(projectImgUrl);
        }

        project.updateProject(patch, projectImgUrl);
        // 프로젝트 기슬스택 수정
        // 기술스택 코드 디테일 조회해서 프로젝트에 추가(이미있는 값이면 addTechCode에서 쳐냄
        patch.totechList().forEach(tech -> {
            CodeDetail techCode = codeDetailRepository.findById(tech.techCode()).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
            ProjectTechCode projectTechCode = new ProjectTechCode(project, techCode);
            if(!project.hasTechCode(projectTechCode)){
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
            if (findTotal.isPresent()){
                ProjectTotal projectTotal = findTotal.get();
                projectTotal.setTotal(total.total());
                projectTotal.setCurrent(total.current());

                projectTotalService.updateProjectTotal(projectTotal);
            }else{ // 새로운 포지션 ProjectTotal 만들어서 save
                CodeDetail detail = codeDetailRepository.findById(total.jobCode()).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
                ProjectTotal projectTotal = new ProjectTotal(project, detail, total.total(), total.current());
                projectTotalService.updateProjectTotal(projectTotal);
                project.addTotal(projectTotal);
            }
        });

        projectRespository.save(project);

        return projectMapper.entityToResponse(project, true);
    }

    public Project deleteProject(Long projectId, Integer memberId) {
        Project project = projectRespository.findById(projectId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), memberId);

        ProjectMember projectMember = projectMemberRespository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        //요청한 멤버가 팀장인지 검사
        if(projectMember.getRoleCode().getId() != 17){
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }
        project.setDeleted(true);

        projectRespository.save(project);

        return project;
    }

    public Project startProject(Long projectId, Integer memberId) {
        // 멤버가 팀장인지 확인
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        Optional<ProjectMember> projectMember = projectMemberRespository.findById(projectMemberId);

        if(!projectMember.isPresent() || !projectMember.get().getRoleCode().getId().equals(17)){
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }

        Project project = projectRespository.findById(projectId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        
        //이미 시작된 프로젝트인지 확인
        if (project.getStartDay() == null){
            project.startProject();
        }else {
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }

        projectRespository.save(project);

        return project;
    }

    public Project endProject(Long projectId, Integer memberId) {
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        Optional<ProjectMember> projectMember = projectMemberRespository.findById(projectMemberId);

        if(!projectMember.isPresent() || projectMember.get().getRoleCode().getId() != 17){
            throw new CommonException(ExceptionEnum.ALREADY_EXIST_START);
        }

        Project project = projectRespository.findById(projectId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        //이미 시작된 프로젝트인지 확인
        if (project.getStartDay() != null || project.getEndDay() == null){
            project.endProject();
        }else {
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
        if(findLike.isPresent()){
            throw new CommonException(ExceptionEnum.ALREADY_EXIST_Like);
        }
        
        Project project = projectRespository.findById(projectId).orElseThrow(()-> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
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

        Project project = projectRespository.findById(projectId).orElseThrow(()-> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        //프로젝트 좋아요 수 -1
        project.deleteLike();

        projectRespository.save(project);

        likeRepository.delete(findLike);
    }
}
