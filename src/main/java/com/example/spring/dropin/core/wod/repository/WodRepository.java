package com.example.spring.dropin.core.wod.repository;

import com.example.spring.dropin.domain.Wod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface WodRepository extends JpaRepository<Wod, Long> {
    /**
     * created 컬럼의 DATE(created) 값이 파라미터와 동일한 레코드만 조회
     *
     * • function('DATE', ...) 은 JPQL에서 SQL 함수 DATE() 를 호출하는 방식입니다.
     * • 파라미터로 LocalDate 를 넘겨주면 내부적으로 yyyy‑MM‑dd 형식으로 비교됩니다.
     */
    @Query("""
      select w
      from Wod w
      where function('DATE', w.created) = :date
        and w.box = :box
    """)
    Optional<Wod> findOneByCreatedDateAndBox(
            @Param("date") LocalDate date,
            @Param("box")  String box
    );
}
