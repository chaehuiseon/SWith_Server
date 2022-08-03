package com.example.demo.src.repository;

import com.example.demo.src.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("select a from Attendance a where a.groupInfo.groupIdx = :groupIdx and a.user.userIdx = :userIdx and a.status <> :status")
    List<Attendance> findByGroupInfo_GroupIdxAndUser_UserIdxAndStatusIsNot(Long groupIdx, Long userIdx, Integer status);

}
