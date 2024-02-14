package com.backend.prog.domain.project.domain;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@ToString(exclude = {"project", "jobCode"})
@NoArgsConstructor
@DynamicInsert
@Table(name = "project_total")
public class ProjectTotal extends BaseEntity {
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
    @Comment("직무코드")
    private CodeDetail jobCode;

    @Comment("총 인원")
    private Integer total;
    @Comment("현재 인원")
    @ColumnDefault("0")
    private Integer current;

    @Builder
    public ProjectTotal(Project project, CodeDetail jobCode, Integer total, Integer current) {
        this.project = project;
        this.jobCode = jobCode;
        this.total = total;
        this.current = current;
        this.id = new ProjectCodeDetaliId(project.getId(), jobCode.getId());
    }

    public ProjectTotal(CodeDetail jobCode, Integer total, Integer current) {
        this.jobCode = jobCode;
        this.total = total;
        this.current = current;
    }

    public void addCurrent(){
        this.current += 1;
    }

    public void subtractCurrent(){
        this.current -= 1;
    }

}
