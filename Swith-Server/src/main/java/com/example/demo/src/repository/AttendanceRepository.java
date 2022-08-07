package com.example.demo.src.repository;

import com.example.demo.src.dto.response.UserAttendanceNum;
import com.example.demo.src.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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
    List<UserAttendanceNum> getUserTotalAttendance(Long groupIdx);

}
