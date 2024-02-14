package com.backend.prog.domain.feed.application;

import com.backend.prog.domain.feed.dto.FeedListResponse;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.project.domain.Project;

import java.util.List;

public interface FeedService {
    void saveFeed(Long contentId, Project project, CodeDetail codeDetail, Member member);

    List<FeedListResponse> getFeeds(Integer memberId, Long projectId);
}
