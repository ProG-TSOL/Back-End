package com.backend.prog.domain.project.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProjectCodeDetaliId implements Serializable {
    private Long projectId;
    private Integer codeId;
}
