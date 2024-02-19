package com.backend.prog.domain.manager.domain;

import com.backend.prog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.springframework.util.StringUtils;

@Entity
@Getter
@ToString(exclude = {"code"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "code_detail")
public class CodeDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    @Comment("상세코드 아이디")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id")
    @Comment("코드 아이디")
    private Code code;

    @Column(length = 30)
    @Comment("상세코드명")
    private String detailName;
    @Column(length = 300)
    @Comment("상세코드 설명")
    private String detailDescription;
    @Column(length = 255)
    @Comment("이미지 주소")
    private String imgUrl;
    @Comment("상세코드 사용 여부")
    @ColumnDefault("true")
    private Boolean isUse;

    @Builder
    private CodeDetail(Code code, String detailName, String detailDescription, String imgUrl) {
        this.code = code;
        this.detailName = detailName;
        this.detailDescription = detailDescription;
        this.imgUrl = imgUrl;
    }

    public void update(String detailName, String detailDescription, String imgUrl, Boolean isUse) {
        if (StringUtils.hasText(detailName)) this.detailName = detailName;
        if (StringUtils.hasText(detailDescription)) this.detailDescription = detailDescription;
        if (StringUtils.hasText(imgUrl)) this.imgUrl = imgUrl;
        if (isUse != null) this.isUse = isUse;
    }
}
