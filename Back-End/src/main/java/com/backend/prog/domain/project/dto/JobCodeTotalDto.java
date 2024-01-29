package com.backend.prog.domain.project.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JobCodeTotalDto {
    private Integer id;
    private Integer total;

    @QueryProjection
    public JobCodeTotalDto(Integer id, Integer total) {
        this.id = id;
        this.total = total;
    }
}
