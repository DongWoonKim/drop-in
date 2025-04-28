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

    /**
     ✅ 이 쿼리가 성능이 좋은 이유
     1.	인덱스를 최적으로 활용할 수 있다
     •	ORDER BY createdAt DESC, id DESC에 맞춰서
     •	(createdAt, id) 복합 인덱스를 걸어두면,
     •	데이터베이스가 정렬없이 인덱스만 타고 빠르게 결과를 가져올 수 있다.
     2.	불필요한 OFFSET 스캔이 없다
     •	보통 OFFSET n 쓰면, DB는 n개를 버리는 작업을 한다. (LIMIT 10 OFFSET 10000 이런 거 심각해짐)
     •	그런데 이 방식은
     “마지막 createdAt/id보다 작은 것만”
     직접 가져오니까
     •	앞의 데이터를 버리는 일이 아예 없음.
     (바로 WHERE 조건으로 ‘건너뛰는’ 거야.)
     3.	Pagination (Keyset Paging, Seek Method) 방식
     •	이걸 “Keyset Pagination” 또는 “Seek Method”라고 부른다.
     •	✅ ‘페이지 번호’ 기반이 아니라 ‘기준 데이터’ 기반으로
     •	훨씬 빠르고 안정적으로 다음 데이터를 가져온다.
     4.	정확하고 일관성 있는 순서 유지
     •	createdAt DESC, id DESC로 정확한 정렬을 보장하면서,
     •	동시 저장된 데이터들도 id까지 비교해 안전하게 순서를 유지할 수 있다.

     ✨ 예를 들어
     id     createdAt
     10     2025-04-28T12:00:00
     9     2025-04-28T12:00:00
     8     2025-04-28T11:59:59

     •	현재 마지막 데이터가 (createdAt=2025-04-28T12:00:00, id=9)이라면,
     •	다음 데이터는 바로
     “createdAt이 작거나, createdAt 같으면 id가 작은 데이터”
     를 정확히 타겟팅한다.

     👉 DB가 이걸 인덱스만 타면서 쭉 스캔할 수 있으니까, 성능이 폭발적으로 좋아진다.

     상황에 딱 맞는 인덱스는 (box_id, created_at, id) 복합 인덱스
     ✅ 그래서
     •	box_id 먼저 필터
     •	created_at로 정렬/필터
     •	id로 정렬/필터

     → 이 순서대로 인덱스 타야 성능이 제일 잘 나온다.

     CREATE INDEX idx_sns_box_created_id
     ON sns (box_id, created_at DESC, id DESC);

     •	인덱스 이름은 idx_sns_box_created_id
     •	컬럼 순서는 box_id → created_at → id
     •	정렬은 DESC 걸어줬어 (왜냐면 네가 ORDER BY createdAt DESC, id DESC 하니까)

     SHOW INDEX FROM sns;

     ✨ EXPLAIN으로 인덱스 타는지 확인하는 방법
     1.	MySQL 콘솔 접속하거나, DBeaver / Workbench 열고
     2.	아래처럼 EXPLAIN 붙여서 쿼리 실행해봐:

     EXPLAIN
     SELECT s.*
     FROM sns s
     WHERE s.box_id = 1
     AND (s.created_at < '2025-04-28T21:00:00'
     OR (s.created_at = '2025-04-28T21:00:00' AND s.id < 100))
     ORDER BY s.created_at DESC, s.id DESC
     LIMIT 3;
     */
    @Query("SELECT s FROM Sns s WHERE s.boxId = :box AND (s.createdAt < :lastCreatedAt OR (s.createdAt = :lastCreatedAt AND s.id < :lastId)) ORDER BY s.createdAt DESC, s.id DESC")
    List<Sns> findByBoxIdAndPaging(@Param("box") Long box,
                                   @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
                                   @Param("lastId") Long lastId,
                                   Pageable pageable);
}
