package com.example.demo.src.repository;

import com.example.demo.src.entity.GroupInfo;
import com.example.demo.src.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findFirstByGroupInfo_GroupIdxAndSessionStartAfterOrderBySessionNum(Long sessionIdx, LocalDateTime currentTime);

    @Query("select s from Session s where s.groupInfo.groupIdx = :groupIdx order by s.sessionStart")
    List<Session> findByGroupIdx(Long groupIdx);

    @Query("select count(s) from Session s where s.groupInfo.groupIdx = :groupIdx " +
            "and s.sessionEnd < :sessionStart")
    Integer findAppropriateSessionNum(Long groupIdx, LocalDateTime sessionStart);

    //벌크성 수정 쿼리
    @Modifying
    @Query("update Session s set s.sessionNum = s.sessionNum + 1 " +
            "where s.groupInfo.groupIdx = :groupIdx and s.sessionNum >= :sessionNum")
    Integer updateSessionNumPlusOne(Integer sessionNum, Long groupIdx);


    @Query("select s from Session s join fetch s.attendances where s.groupInfo.groupIdx = :groupIdx")
    List<Session> getSessionInfoByGroupIdx(Long groupIdx);

    @Query("select s from Session s " +
            "join fetch s.groupInfo " +
            "where s.sessionIdx = :sessionIdx")
    Optional<Session> findByIdWithGroup(Long sessionIdx);

//    @Query("update Session s set s.sessionNum = s.sessionNum + :num " +
//            "where s.groupInfo.groupIdx = :groupIdx " +
//            "and s.sessionNum > :start " +
//            "and s.sessionNum < :end ")
//    Integer updateSessionNum(Integer start, Integer end, Integer num, Long groupIdx);

    @Query("select (count(s) > 0) from Session s " +
            "where s.groupInfo.groupIdx = :groupIdx and " +
            "s.sessionIdx <> :sessionIdx and " +
            "((s.sessionStart <= :start and s.sessionEnd > :start ) or" +
            "(:start < s.sessionStart and :end > s.sessionStart )) ")
    boolean existsOverlappedSession(Long groupIdx, Long sessionIdx, LocalDateTime start, LocalDateTime end);
}
