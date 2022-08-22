package com.example.demo.src.repository;

import com.example.demo.src.entity.Session;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findFirstByGroupInfo_GroupIdxAndSessionStartAfterAndStatusEqualsOrderBySessionNum(
            Long sessionIdx, LocalDateTime currentTime,Integer status);


    @Query("select s from Session s " +
            "where s.groupInfo.groupIdx = :groupIdx " +
            "and s.status = 0" +
            "order by s.sessionStart")
    List<Session> findByGroupIdx(Long groupIdx);

    @Query("select count(s) from Session s " +
            "where s.groupInfo.groupIdx = :groupIdx " +
            "and s.sessionEnd < :sessionStart " +
            "and s.status = 0")
    Integer findAppropriateSessionNum(Long groupIdx, LocalDateTime sessionStart);

    //벌크성 수정 쿼리
    @Modifying
    @Query("update Session s set s.sessionNum = s.sessionNum + 1 " +
            "where s.groupInfo.groupIdx = :groupIdx " +
            "and s.sessionNum >= :sessionNum " +
            "and s.status = 0")
    Integer updateSessionNumPlusOne(Integer sessionNum, Long groupIdx);


    @Query("select distinct s from Session s " +
            "left join fetch s.attendances " +
            "where s.groupInfo.groupIdx = :groupIdx " +
            "and s.status = 0 ")
    List<Session> getSessionAndAttendanceByGroupIdx(Long groupIdx);

    @Query("select s from Session s " +
            "join fetch s.groupInfo " +
            "where s.sessionIdx = :sessionIdx")
    Optional<Session> findByIdWithGroup(Long sessionIdx);

    @Query("select (count(s) > 0) from Session s " +
            "where s.groupInfo.groupIdx = :groupIdx and " +
            "s.sessionIdx <> :sessionIdx and " +
            "s.status = 0 and " +
            "((s.sessionStart <= :start and s.sessionEnd > :start ) or" +
            "(:start < s.sessionStart and :end > s.sessionStart )) ")
    boolean existsOverlappedSession(Long groupIdx, Long sessionIdx, LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query("update Session s set s.status = 1 " +
            "where s.sessionIdx = :sessionIdx ")
    Integer deleteSession(Long sessionIdx);

    //결석 케이스 조회
    @Query("select distinct s from Session s " +
            "left join fetch s.attendances " +
            "where s.groupInfo.status = 1 " +
            "and s.sessionEnd < :now " +      //지금 이전에 끝나고
            "and s.sessionStart > :limit " +      //limit 이후에 시작한 회차
            "and s.status = 0")
    List<Session> getAllWithGroupAndAttendances(LocalDateTime now, LocalDateTime limit);

}
