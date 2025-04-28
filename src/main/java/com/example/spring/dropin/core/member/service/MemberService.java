package com.example.spring.dropin.core.member.service;

import com.example.spring.dropin.config.security.CustomUserDetails;
import com.example.spring.dropin.core.member.dto.UserStatusResponseDTO;
import com.example.spring.dropin.domain.Member;
import com.example.spring.dropin.core.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;

    public Member registerMember(Member member) {
        return memberRepository.save(member);
    }

    public Member loginMember(Member member) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        member.getUserId(),
                        member.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        return ((CustomUserDetails) authenticate.getPrincipal()).getMember();
    }

    public Member getUser(String userId) {
        return memberRepository.findByUserId(userId);
    }

}
