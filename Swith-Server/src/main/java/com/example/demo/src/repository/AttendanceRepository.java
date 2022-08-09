package com.example.demo.src.repository;

import com.example.demo.src.dto.response.UserAttendanceInfo;
import com.example.demo.src.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("select a from Attendance a where a.groupInfo.groupIdx = :groupIdx " +
            "and a.user.userIdx = :userIdx and a.status <> :status")
    List<Attendance> findByGroupIdxAndUserIdxAndStatusIsNot(Long groupIdx, Long userIdx, Integer status);

    @Query("select a from Attendance a where a.groupInfo.groupIdx = :groupIdx and a.status <> :status")
    List<Attendance> findByGroupIdxAndStatusIsNot(Long groupIdx, Integer status);

    @Query("select a.user.userIdx as userIdx , a.user.nickname as nickname , " +
            "a.status as status , count(a) as attendanceNum " +
            "from Attendance a " +
            "where a.groupInfo.groupIdx = :groupIdx " +
            "group by a.user.userIdx , a.status " +
            "order by a.user.userIdx")
    List<UserAttendanceInfo> getUserTotalAttendance(Long groupIdx);

    @Query("select a from Attendance a " +
            "join fetch a.user " +
            "where a.session.sessionIdx = :sessionIdx")
    List<Attendance> findBySession(Long sessionIdx);

//    @Query("update Attendance a set a.status = :status " +
//            "where a.user.userIdx = :userIdx and a.session.sessionIdx = :sessionIdx")
//    Long updateAttendanceStatus(Integer status, Long userIdx, Long sessionIdx);

    @Query("select a from Attendance a " +
            "where a.user.userIdx = :userIdx and a.session.sessionIdx = :sessionIdx")
    Optional<Attendance> findByUserAndSession(Long userIdx, Long sessionIdx);

    @Query("select (count(a) > 0) from Attendance a " +
            "where a.user.userIdx = :userIdx " +
            "and a.session.sessionIdx = :sessionIdx")
    boolean existsByUserAndSession(Long userIdx, Long sessionIdx);

}
