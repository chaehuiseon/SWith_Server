package com.example.demo.src.repository;

import com.example.demo.src.entity.GroupInfo;
import com.example.demo.src.entity.Register;
import com.example.demo.src.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RegisterRepository extends JpaRepository<Register,Long> {

    List<Register> findByUser_UserIdxAndStatusEquals(Long userIdx, Integer status);

    //Status = 0(가입)인 그룹을 List 형태로 userIdx를 기준으로 불러온다.
    @Query("select g from Register r " +
            "join GroupInfo g on g = r.groupInfo " +
            "where r.user.userIdx = :userIdx " +
            "and r.status = 0 " +
            "and r.groupInfo.status <> 2")
    List<GroupInfo> findGroupInfoByUserIdx(@Param("userIdx")Long userIdx);

    @Query("select r.user " +
            "from Register r " +
            "where r.groupInfo.groupIdx = :groupIdx")
    List<User> findUserByGroup(Long groupIdx);

    @Query("select r " +
            "from Register r " +
            "where r.groupInfo.groupIdx = :groupIdx and r.status = 0")
    List<Register> findUser(@Param("groupIdx") Long groupIdx);


    //    @Query("select u " +
//            "from Register r " +
//            "join fetch r.user u " +
//            "where r.groupInfo.groupIdx = :groupIdx and r.status = 0")
//    List<User> findRegisterUser(@Param("groupIdx")Long groupIdx);






}
