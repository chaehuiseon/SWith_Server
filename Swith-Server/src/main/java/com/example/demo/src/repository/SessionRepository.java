package com.example.demo.src.repository;

import com.example.demo.src.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    List<Session> findByGroupIdx(@Param("groupIdx") Long groupIdx);

    @Query("select count(s) from Session s " +
            "where s.groupInfo.groupIdx = :groupIdx " +
            "and s.sessionEnd < :sessionStart " +
            "and s.status = 0")
    Integer findAppropriateSessionNum(@Param("groupIdx") Long groupIdx,
                                      @Param("sessionStart")LocalDateTime sessionStart);

    //벌크성 수정 쿼리
    @Modifying
    @Query("update Session s set s.sessionNum = s.sessionNum + 1 " +
            "where s.groupInfo.groupIdx = :groupIdx " +
            "and s.sessionNum >= :sessionNum " +
            "and s.status = 0")
    Integer updateSessionNumPlusOne(@Param("sessionNum") Integer sessionNum,
                                    @Param("groupIdx") Long groupIdx);


    @Query("select s from Session s " +
            "left join fetch s.attendances " +
            "where s.groupInfo.groupIdx = :groupIdx " +
            "and s.status = 0 ")
    List<Session> getSessionAndAttendanceByGroupIdx(@Param("groupIdx") Long groupIdx);

    @Query("select s from Session s " +
            "join fetch s.groupInfo " +
            "where s.sessionIdx = :sessionIdx")
    Optional<Session> findByIdWithGroup( @Param("sessionIdx") Long sessionIdx);

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
    boolean existsOverlappedSession(@Param("groupIdx") Long groupIdx,
                                    @Param("sessionIdx") Long sessionIdx,
                                    @Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end);

    @Query("update Session s set s.status = 1 " +
            "where s.sessionIdx = :sessionIdx ")
    Long deleteSession(@Param("sessionIdx") Long sessionIdx);
}
