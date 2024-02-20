package com.backend.prog.domain.feed.application;

import com.backend.prog.domain.feed.dao.FeedRepository;
import com.backend.prog.domain.feed.domain.Feed;
import com.backend.prog.domain.feed.dto.FeedListResponse;
import com.backend.prog.domain.feed.dto.KafkaFeedDto;
import com.backend.prog.domain.manager.dao.CodeDetailRepository;
import com.backend.prog.domain.manager.dao.CodeRepository;
import com.backend.prog.domain.manager.domain.Code;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.project.dao.ProjectMemberRespository;
import com.backend.prog.domain.project.dao.ProjectRespository;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.work.dao.WorkRepository;
import com.backend.prog.domain.work.domain.Work;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedServiceimpl implements FeedService {

    private final FeedRepository feedRepository;
    private final ProjectRespository projectRespository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRespository projectMemberRespository;
    private final WorkRepository workRepository;
    private final CodeRepository codeRepository;
    private final CodeDetailRepository codeDetailRepository;

    private final KafkaTemplate<String, KafkaFeedDto> kafkaTemplate;

    /**
     * 업무, 회고, 게시글에서 넘긴정보로 feedDto 생성
     *
     * @param codedetailName : 컨텐츠 구분코드명
     * @param feedDtoMap     : 컨텐츠 정보
     */
    public void makeFeedDto(String codedetailName, Map<String, Object> feedDtoMap) {
        Code code = codeRepository.findByName("Content");
        CodeDetail contentsCode = codeDetailRepository.findByCodeAndDetailName(code, codedetailName);

        KafkaFeedDto kafkaDto = KafkaFeedDto.builder()
                .projectId((Long) feedDtoMap.get("projectId"))
                .contentsCode(contentsCode.getId())
                .contentsId((Long) feedDtoMap.get("contentsId"))
                .memberId((Integer) feedDtoMap.get("memberId"))
                .build();

        String topic = "feed_created";
        kafkaTemplate.send(topic, kafkaDto);
    }

    @KafkaListener(topics = "feed_created", groupId = "group_1")
    @Transactional
    public void consume(KafkaFeedDto kafkaFeedDto) {
        Project project = projectRespository.findById(kafkaFeedDto.getProjectId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        CodeDetail codeDetail = codeDetailRepository.findById(kafkaFeedDto.getContentsCode())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        Member member = memberRepository.findById(kafkaFeedDto.getMemberId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        // 해당 프로젝트에 속한인원에게 전부?, 글 올린 당사자는 피드 생성x
        saveFeed(kafkaFeedDto.getContentsId(), project, codeDetail, member);
    }

    @Transactional
    @Override
    public void saveFeed(Long contentId, Project project, CodeDetail codeDetail, Member member) {
        String detailName = codeDetail.getDetailName();
        StringBuilder sb = new StringBuilder();

        if (detailName.equals("Work")) {
            sb.append("[업무] ").append(member.getNickname()).append("님이 업무를 요청했습니다.");
            Work work = workRepository.findById(contentId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

            Feed feed = Feed.builder()
                    .project(project)
                    .contentCode(codeDetail)
                    .contentId(contentId)
                    .memberId(work.getConsumerId().getId())
                    .content(sb.toString())
                    .build();
            feedRepository.save(feed);

        } else {
            if (detailName.equals("Post")) {
                sb.append("[게시글] ").append(member.getNickname()).append("님이 게시글을 작성했습니다.");
            } else {
                sb.append("[회고] ").append(member.getNickname()).append("님이 회고를 작성했습니다.");
            }

            // 작성자 제외 같은 프로젝트에 속해있는 member들에게 피드 생성
            projectMemberRespository.findAllByProjectAndNotDeleted(project)
                    .stream()
                    .filter(projectMember -> !projectMember.getMember().getId().equals(member.getId()))
                    .forEach(projectMember -> {
                        Feed feed = Feed.builder()
                                .project(project)
                                .contentCode(codeDetail)
                                .contentId(contentId)
                                .memberId(projectMember.getMember().getId())
                                .content(sb.toString())
                                .build();
                        feedRepository.save(feed);
                    });
        }


    }

    @Override
    public List<FeedListResponse> getFeeds(Integer memberId, Long projectId) {
        List<Feed> feedList = feedRepository.findByProjectMember(memberId, projectId);

        return feedList.stream()
                .map(feed -> {
                    Member member = memberRepository.findById(feed.getMemberId())
                            .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
                    return FeedListResponse.builder()
                            .feedId(feed.getId())
                            .contentsCode(feed.getContentCode().getId())
                            .contentsId(feed.getContentId())
                            .memberImgUrl(member.getImgUrl())
                            .feedContent(feed.getContent())
                            .build();
                }).toList();
    }
}
