package com.example.demo.src.repository;

import com.example.demo.src.dto.GetSessionRes;
import com.example.demo.src.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findFirstByGroupInfo_GroupIdxAndSessionStartAfterOrderBySessionNum(Long sessionIdx, LocalDateTime currentTime);

    @Query("select s from Session s where s.groupInfo.groupIdx = ?1")
    List<Session> findByGroupIdx(Long groupIdx);

    @Query("select count(s) from Session s where s.groupInfo.groupIdx = :groupIdx " +
            "and s.sessionEnd < :sessionStart")
    Integer findAppropriateSessionNum(Long groupIdx, LocalDateTime sessionStart);

    @Modifying
    @Query("update Session s set s.sessionNum = s.sessionNum + 1 " +
            "where s.groupInfo.groupIdx = :groupIdx and s.sessionNum >= :sessionNum")
    Integer UpdateSessionNumPlusOne(Integer sessionNum, Long groupIdx);


    @Query("select s from Session s join fetch s.attendances where s.groupInfo.groupIdx = :groupIdx")
    List<Session> GetSessionInfoByGroupIdx(Long groupIdx);
}
