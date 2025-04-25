package com.example.spring.dropin.core.record.repository;

import com.example.spring.dropin.domain.Record;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
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

    @Query("""
      select r
        from Record r
       where function('DATE', r.date) = :date
         and r.box    = :box
       order by r.userId asc
    """)
    Optional<List<Record>> findByDateAndBox(
            @Param("date") LocalDate date,
            @Param("box")    String    box
    );

    @Modifying
    @Transactional
    @Query("UPDATE Record r SET r.content = :content WHERE r.id = :id AND r.userId = :userId")
    int updateContentByIdAndUserId(@Param("id") Long id,
                                   @Param("userId") String userId,
                                   @Param("content") String content
    );

}
