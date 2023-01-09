package com.swith.domain.register.repository;

import com.swith.domain.groupinfo.entity.GroupInfo;
import com.swith.domain.register.entity.Register;
import com.swith.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public interface RegisterRepository extends JpaRepository<Register,Long> {

    List<Register> findByUser_UserIdxAndStatusEquals(Long userIdx, Integer status);

    //Status = 0(가입), 1(진행중)인 그룹을 List 형태로 userIdx를 기준으로 불러온다.
    @Query("select g from Register r " +
            "join GroupInfo g on g = r.groupInfo " +
            "where r.user.userIdx = :userIdx " +
            "and r.status = 0 " +
            "and r.groupInfo.status <> 2")
    List<GroupInfo> findGroupInfoByUserIdx(@Param("userIdx")Long userIdx);

    @Query("select r.user " +
            "from Register r " +
            "where r.groupInfo.groupIdx = :groupIdx " +
            "and r.status = 0")
    List<User> findUserByGroup(Long groupIdx);


    @Query("select r.user.userIdx " +
            "from Register r " +
            "where r.groupInfo.groupIdx = :groupIdx " +
            "and r.status = 0")
    ArrayList<Long> findUserByGroup2(Long groupIdx);

    @Query("select r.user.userIdx " +
            "from Register r " +
            "where r.groupInfo.groupIdx = :groupIdx")
    ArrayList<Long> findUserIdxByGroup(@Param("groupIdx") Long groupIdx);

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
