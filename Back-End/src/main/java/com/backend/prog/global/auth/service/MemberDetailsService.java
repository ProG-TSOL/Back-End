package com.backend.prog.global.auth.service;

import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.global.auth.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.getWithRoles(email).orElseThrow(() -> new UsernameNotFoundException("Email Not Found"));

        return new MemberSecurityDTO(member);
    }
}
