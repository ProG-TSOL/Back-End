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
@Table(name = "member")
@ToString
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
    @Comment("깃허브 아이디")
    private String gitUsername;

    @Column(length = 255)
    @Comment("원래 이미지 이름")
    private String originImg;

    @Column(length = 255)
    @Comment("이미지주소")
    private String imgUrl;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    @Builder
    public Member(String email, String password, String name, String nickname) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
    }

    public void updateProfileInfo(String name, String nickname, String description) {
        this.name = name;
        this.nickname = nickname;
        this.description = description;
    }

    public void updateProfileImg(String originImg, String imgUrl) {
        this.originImg = originImg;
        this.imgUrl = imgUrl;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void addProvider(Provider provider) {
        this.provider = provider;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void changeGithubInfo(String gitUsername) {
        this.gitUsername = gitUsername;
    }

    @Override
    public void deleteData() {
        super.deleteData();
        this.roles = null;
        this.nickname = "탈퇴회원";
    }
}