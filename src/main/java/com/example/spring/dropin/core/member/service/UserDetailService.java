package com.example.spring.dropin.core.member.service;

import com.example.spring.dropin.config.security.CustomUserDetails;
import com.example.spring.dropin.domain.Member;
import com.example.spring.dropin.core.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUserId(username);
        System.out.println("UserDetailService :: " + member);
        if (member == null) {
            throw new UsernameNotFoundException(username + " not found");
        }

        return CustomUserDetails.builder()
                .member(member)
                .roles(List.of(String.valueOf(member.getRole())))
                .build(); // Member 정보를 가진 CustomUserDetails 반환
    }
}
