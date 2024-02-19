package com.backend.prog.global.auth.dto;

import com.backend.prog.domain.member.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.stream.Collectors;

@Getter
public class MemberSecurityDTO extends User {

    private final Integer id;

    public MemberSecurityDTO(Member member) {
        super(member.getEmail(), member.getPassword(), member.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList()));
        this.id = member.getId();
    }
}
