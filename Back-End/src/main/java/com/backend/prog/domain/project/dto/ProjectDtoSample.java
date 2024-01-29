package com.backend.prog.domain.project.dto;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class ProjectDtoSample {
    private Long id;
    private String teamName;
    private String title;
    private String content;
    private LocalDate startDay;
    private LocalDate endDay;
    private Integer viewCnt;
    private Integer likeCnt;
    private Integer period;
    private String projectImgUrl;
    private List<CodeDetail> techCodes;

    @QueryProjection
    public ProjectDtoSample(Long id, String teamName, String title, String content, LocalDate startDay, LocalDate endDay,
                            Integer viewCnt, Integer likeCnt, Integer period, String projectImgUrl, List<CodeDetail> techCodes) {
        this.id = id;
        this.teamName = teamName;
        this.title = title;
        this.content = content;
        this.startDay = startDay;
        this.endDay = endDay;
        this.viewCnt = viewCnt;
        this.likeCnt = likeCnt;
        this.period = period;
        this.projectImgUrl = projectImgUrl;
        this.techCodes = techCodes;
    }
}
