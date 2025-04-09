package com.example.spring.dropin.member.repository;

import com.example.spring.dropin.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUserId(String userId);
}
