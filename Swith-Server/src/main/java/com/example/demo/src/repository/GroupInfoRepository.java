package com.example.demo.src.repository;


import com.example.demo.src.entity.GroupInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupInfoRepository extends JpaRepository<GroupInfo, Long>, GroupInfoRepositoryCustom {
    GroupInfo findByGroupIdx(Long groupIdx);

    @Query(value = "select g.memberLimit " +
            "from GroupInfo g "+
            "where g.groupIdx = :groupIdx")
    Integer findMemberLimitBy(@Param("groupIdx") Long groupIdx);


    @Query("select g.status from GroupInfo g "+
            "where g.groupIdx = :groupIdx")
    Integer findstatusOfGroupInfo(@Param("groupIdx") Long groupIdx);

    @Query("select g.user.userIdx from GroupInfo g where g.groupIdx = :groupIdx")
    Long findAdminIdxBy(@Param("groupIdx")Long groupIdx);



}
