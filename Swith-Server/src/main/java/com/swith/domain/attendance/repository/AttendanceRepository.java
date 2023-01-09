package com.swith.domain.attendance.repository;

import com.swith.api.dto.response.UserAttendanceInfo;
import com.swith.domain.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("select a from Attendance a where a.groupInfo.groupIdx = :groupIdx " +
            "and a.user.userIdx = :userIdx and a.status <> :status")
    List<Attendance> findByGroupIdxAndUserIdxAndStatusIsNot(@Param("groupIdx") Long groupIdx,
                                                            @Param("userIdx") Long userIdx,
                                                            @Param("status")Integer status);

    @Query("select a from Attendance a " +
            "where a.groupInfo.groupIdx = :groupIdx " +
            "and a.status <> :status")
    List<Attendance> findByGroupIdxAndStatusIsNot(@Param("groupIdx") Long groupIdx,
                                                  @Param("status")Integer status);

    @Query("select a from Attendance a " +
            "join fetch a.session " +
            "where a.groupInfo.groupIdx = :groupIdx " +
            "and a.status = 0")
    List<Attendance> testFind(Long groupIdx);


    @Query("select a.user.userIdx as userIdx , a.user.nickname as nickname , " +
            "a.status as status , count(a) as attendanceNum " +
            "from Attendance a " +
            "where a.groupInfo.groupIdx = :groupIdx " +
            "group by a.user.userIdx , a.status " +
            "order by a.user.userIdx")
    List<UserAttendanceInfo> getUserTotalAttendance(@Param("groupIdx") Long groupIdx);

    @Query("select a from Attendance a " +
            "join fetch a.user " +
            "where a.session.sessionIdx = :sessionIdx")
    List<Attendance> findBySession(@Param("sessionIdx") Long sessionIdx);

//    @Query("update Attendance a set a.status = :status " +
//            "where a.user.userIdx = :userIdx and a.session.sessionIdx = :sessionIdx")
//    Long updateAttendanceStatus(Integer status, Long userIdx, Long sessionIdx);

    @Query("select a from Attendance a " +
            "where a.user.userIdx = :userIdx and a.session.sessionIdx = :sessionIdx")
    Optional<Attendance> findByUserAndSession(@Param("userIdx") Long userIdx, @Param("sessionIdx") Long sessionIdx);

    @Modifying
    @Query("update Attendance a set a.status = :status " +
            "where a.attendanceIdx = :attendanceIdx ")
    Integer modifyStatus(@Param("attendanceIdx") Long attendanceIdx, @Param("status") Integer status);

//    @Query("select (count(a) > 0) from Attendance a " +
//            "where a.user.userIdx = :userIdx " +
//            "and a.session.sessionIdx = :sessionIdx")
//    boolean existsByUserAndSession(Long userIdx, Long sessionIdx);

}
