package com.backend.prog.domain.member.domain;

import com.backend.prog.global.common.DeleteEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"roles"})
@Table(name = "member")
public class Member extends DeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    @Comment("회원ID")
    private Integer id;

    @Column(length = 100)
    @Comment("이메일")
    private String email;

    @Column(length = 64)
    @Comment("비밀번호")
    private String password;

    @Enumerated(EnumType.STRING)
    @Comment("소셜로그인 사이트")
    private Provider provider;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    @Column(length = 30)
    @Comment("이름")
    private String name;

    @Column(length = 30)
    @Comment("닉네임")
    private String nickname;

    @Column(length = 150)
    @Comment("한줄소개")
    private String description;

    @Column(length = 20)
    @Comment("깃허브 이메일")
    private String gitEmail;

    @Column(length = 20)
    @Comment("깃허브 아이디")
    private String gitId;

    @Column(length = 255)
    @Comment("이미지주소")
    private String imgUrl;

    @Builder
    private Member(String email, String password, Provider provider, String name, String nickname, String description, String gitEmail, String gitId, String imgUrl) {
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.name = name;
        this.nickname = nickname;
        this.description = description;
        this.gitEmail = gitEmail;
        this.gitId = gitId;
        this.imgUrl = imgUrl;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void addProvider(Provider provider) {
        this.provider = provider;
    }

    @Override
    public void deleteData() {
        super.deleteData();
        this.nickname = "알 수 없음";
    }
}
