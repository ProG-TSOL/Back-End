package com.backend.prog.global.auth.service;

import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.member.domain.Provider;
import com.backend.prog.domain.member.domain.Role;
import com.backend.prog.global.auth.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OAuth2MemberService {

    private final MemberRepository memberRepository;

    private static final List<String> ADJECTIVE_LIST = Arrays.asList("행복한", "우울한", "쓸쓸한", "무거운", "따뜻한", "재밌는", "훌륭한", "잘생긴", "어여쁜", "귀여운", "친절한", "순수한", "상냥한",
            "정직한", "성실한", "공정한", "뚱뚱한", "우아한", "즐거운", "이상한", "유명한", "수줍은", "특별한", "피곤한", "솔직한", "심각한", "활발한", "무례한",
            "격분한", "무서운", "어색한", "가엾은", "실망한", "대담한", "용감한", "화려한", "건방진", "강력한");

    private static final List<String> TERM_LIST = Arrays.asList("고양이", "강아지", "거북이", "토끼", "뱀", "사자", "호랑이", "표범", "치타", "하이에나", "기린", "코끼리", "코뿔소", "하마", "악어",
            "펭귄", "부엉이", "올빼미", "곰", "돼지", "소", "닭", "독수리", "타조", "고릴라", "오랑우탄", "침팬지", "원숭이", "코알라", "캥거루", "고래", "상어", "칠면조", "직박구리", "쥐", "청설모",
            "메추라기", "앵무새", "삵", "스라소니", "판다", "오소리", "오리", "거위", "백조", "두루미", "고슴도치", "두더지", "아홀로틀", "맹꽁이", "너구리", "개구리", "두꺼비", "카멜레온", "이구아나",
            "노루", "제비", "까치", "고라니", "수달", "당나귀", "순록", "염소", "공작", "바다표범", "들소", "박쥐", "참새", "물개", "바다사자", "살모사", "구렁이", "얼룩말", "산양", "멧돼지", "카피바라",
            "도롱뇽", "북극곰", "퓨마", "미어캣", "코요테", "라마", "딱따구리", "기러기", "비둘기", "스컹크", "돌고래", "까마귀", "매", "낙타", "여우", "사슴", "늑대", "재규어", "알파카", "양", "다람쥐", "담비");

    @Transactional
    public MemberSecurityDTO loadMember(String provider, String email, String username) {
        Optional<Member> memberOptional = memberRepository.getWithRoles(email);
        Member member = Member.builder().build();

        if(memberOptional.isPresent() && memberOptional.get().isDeleted()) {
            throw new UsernameNotFoundException("withdrawal");
        }

        if (memberOptional.isEmpty()) {
            member = saveMember(email, provider, username);
        }
        else if(memberOptional.get().getProvider() == null || !provider.equals(memberOptional.get().getProvider().getProvider())){
            throw new UsernameNotFoundException("already");
        }
        else {
            member = memberOptional.get();

            if(provider.equals("github") && !member.getGitUsername().equals(username)) {
                member.changeGithubInfo(username);
            }
        }

        return new MemberSecurityDTO(member);
    }

    @Transactional
    public Member saveMember(String email, String provider, String username) {
        Member member = Member.builder()
                .email(email)
                .name("박개굴")
                .nickname(randomNickname())
                .password(UUID.randomUUID().toString())
                .build();

        member.addProvider(Provider.valueOf(provider.toUpperCase()));

        member.addRole(Role.USER);

        if(provider.equals("github")) {
            member.changeGithubInfo(username);
        }

        memberRepository.save(member);

        return member;
    }

    public String randomNickname() {
        Random random = new Random();

        while (true) {
            StringBuilder nickname = new StringBuilder();
            nickname.append(ADJECTIVE_LIST.get(random.nextInt(0, ADJECTIVE_LIST.size()))).append(TERM_LIST.get(random.nextInt(0, TERM_LIST.size())));
            int len = random.nextInt(1, 8);

            for(int i = 0; i < len; i++) {
                nickname.append(random.nextInt(0, 10));
            }

            if(memberRepository.findByNickname(nickname.toString()).isEmpty()) {
                return nickname.toString();
            }
        }
    }
}