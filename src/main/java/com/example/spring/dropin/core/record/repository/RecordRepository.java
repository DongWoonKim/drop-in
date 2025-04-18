package com.example.spring.dropin.core.record.repository;

import com.example.spring.dropin.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    @Query("""
      select r
        from Record r
       where r.userId = :userId
         and function('DATE', r.date) = :date
         and r.box    = :box
    """)
    Optional<Record> findByUserIdAndDateAndBox(
            @Param("userId") String    userId,
            @Param("date") LocalDate date,
            @Param("box")    String    box
    );
}
