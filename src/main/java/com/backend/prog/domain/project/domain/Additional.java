package com.backend.prog.domain.project.domain;

import com.backend.prog.domain.project.dto.AdditionalDto;
import com.backend.prog.global.common.DeleteEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Entity
@Getter
@Setter
@ToString(exclude = {"project"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "additional")
public class Additional extends DeleteEntity {
    @Id
    @Column(name = "additional_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("부가정보ID")
    private Long id;

    @Column(length = 150)
    @Comment("제목")
    private String title;

    @Comment("추가정보 URL")
    private String url;

    @Comment("이미지 URL")
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @Comment("프로젝트ID")
    private Project project;

    @Builder
    public Additional(String title, String url, Project project) {
        this.title = title;
        this.url = url;
        this.project = project;
    }

    public Additional(AdditionalDto.Post post, String imgUrl, Project project){
        Optional.of(post.title()).ifPresent(this::setTitle);
        Optional.of(post.url()).ifPresent(this::setUrl);
        Optional.of(imgUrl).ifPresent(this::setImgUrl);
        Optional.of(project).ifPresent(this::setProject);
    }

    public void updateAdditional(AdditionalDto.Patch patch, String imgUrl){
        Optional.of(patch.title()).ifPresent(this::setTitle);
        Optional.of(patch.url()).ifPresent(this::setUrl);
        Optional.of(imgUrl).ifPresent(this::setImgUrl);
    }
}
