package com.backend.prog.domain.member.domain;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member_tech")
public class MemberTech extends BaseEntity {

    @EmbeddedId
    private MemberTechId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Comment("멤버ID")
    private Member member;

    @MapsId("techCode")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_code")
    @Comment("기술스택코드")
    private CodeDetail techCode;

    @Comment("기술스택수준")
    private Integer techLevel;

    @Builder
    private MemberTech(Member member, CodeDetail techCode, Integer techLevel) {
        this.member = member;
        this.techCode = techCode;
        this.techLevel = techLevel;
    }
}
