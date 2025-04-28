package com.example.spring.dropin.core.sns.repository;

import com.example.spring.dropin.domain.Sns;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnsRepository extends JpaRepository<Sns, Long> {
}
