package com.backend.prog.domain.feed.api;

import com.backend.prog.domain.feed.application.FeedService;
import com.backend.prog.domain.feed.dto.FeedListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
public class FeedController {

    private final FeedService feedService;

    // 피드 목록 조회
    @GetMapping
    public List<FeedListResponse> getFeeds(@RequestParam("memberId") Integer memberId,
                                           @RequestParam("projectId") Long projectId) {
        return feedService.getFeeds(memberId, projectId);
    }
}
