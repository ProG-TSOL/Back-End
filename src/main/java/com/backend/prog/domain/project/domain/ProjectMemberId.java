package com.backend.prog.domain.project.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProjectMemberId implements Serializable {
    private Long projectId;
    private Integer memberId;
}
