package com.backend.prog.domain.manager.domain;

import com.backend.prog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.springframework.util.StringUtils;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "code")
public class Code extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    @Comment("코드 아이디")
    private Integer id;

    @Column(length = 30, unique = true)
    @Comment("코드명")
    private String name;
    @Column(length = 300)
    @Comment("코드 설명")
    private String description;

    @ColumnDefault("true")
    @Comment("코드 사용 여부")
    private Boolean isUse;

    @Builder
    private Code(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void update(String name, String description, Boolean isUse) {
        if (StringUtils.hasText(name)) this.name = name;
        if (StringUtils.hasText(description)) this.description = description;
        if (isUse != null) this.isUse = isUse;
    }
}
