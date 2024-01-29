package com.backend.prog.domain.project.domain;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@ToString(exclude = {"project", "techCode"})
@NoArgsConstructor
@DynamicInsert
@Table(name = "project_tech")
public class ProjectTechCode extends BaseEntity {
    @EmbeddedId
    private ProjectCodeDetaliId id;

    @MapsId("projectId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @Comment("프로젝트ID")
    private Project project;
    @MapsId("codeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id")
    @Comment("기술스택")
    private CodeDetail techCode;

    @Builder
    public ProjectTechCode(Project project, CodeDetail techCode) {
        this.project = project;
        this.techCode = techCode;
        this.id = new ProjectCodeDetaliId(project.getId(), techCode.getId());
    }
}
