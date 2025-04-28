package com.example.spring.dropin.core.sns.repository;

import com.example.spring.dropin.domain.Sns;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SnsRepository extends JpaRepository<Sns, Long> {
    @Query("SELECT s FROM Sns s WHERE s.boxId = :box ORDER BY s.createdAt DESC, s.id DESC")
    List<Sns> findFirstByBoxIdOrderByCreatedAtDescIdDesc(@Param("box") Long box, Pageable pageable);

    @Query("SELECT s FROM Sns s WHERE s.boxId = :box AND (s.createdAt < :lastCreatedAt OR (s.createdAt = :lastCreatedAt AND s.id < :lastId)) ORDER BY s.createdAt DESC, s.id DESC")
    List<Sns> findByBoxIdAndPaging(@Param("box") Long box,
                                   @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
                                   @Param("lastId") Long lastId,
                                   Pageable pageable);
}
