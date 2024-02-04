package com.backend.prog.global.auth.handler;

import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import com.backend.prog.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtUtil jwtUtil;

    private final MemberRepository memberRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String path = request.getRequestURI();

        try {
            String refreshToken = jwtUtil.extractRefreshToken(request.getCookies());

            Map<String, Object> claim = jwtUtil.extractClaim(refreshToken);

            Integer id = (Integer) claim.get("id");

            jwtUtil.destroyRefreshToken(response, id);

            if(path.contains("/withdrawal-member")) {
                deleteMember(id);
            }
        } catch (Exception exception) {
            log.info(exception);
            throw new CommonException(ExceptionEnum.INVALID_REFRESH_TOKEN);
        }

        try {
            String accessToken = jwtUtil.extractAccessToken(request);

            Map<String, Object> claim = jwtUtil.extractClaim(accessToken);

            Integer id = (Integer) claim.get("id");
            Integer exp = (Integer) claim.get("exp");

            jwtUtil.destroyAccessToken(accessToken, id, jwtUtil.getExpiration(exp));
        }  catch (Exception exception) {
        }
    }

    @Transactional
    public void deleteMember(Integer id) {
        Member member = memberRepository.findByIdAndDeletedNot(id).orElseThrow(() -> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));

        member.deleteData();

        memberRepository.save(member);
    }
}