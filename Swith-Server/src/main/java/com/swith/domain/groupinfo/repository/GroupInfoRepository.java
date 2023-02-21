package com.swith.domain.groupinfo.repository;


import com.swith.domain.groupinfo.entity.GroupInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

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

    @Modifying(clearAutomatically = true)
    @Query(value = "update GROUPINFO g " +
            "set g.status = :reqstatus " +
            "where g.groupIdx = :groupIdx and g.adminIdx = :userIdx ",nativeQuery = true)
    void changeGroupInfoStatusEnd(@Param("reqstatus")Integer reqstatus, @Param("groupIdx")Long groupIdx,
                                      @Param("userIdx")Long userIdx);



    @Query("select g.status from GroupInfo g where g.groupIdx = :groupIdx ")
    Integer findStatusOfGroupInfo(@Param("groupIdx")Long groupIdx);


    @Query("select a.user.userIdx from Application a " +
            "where a.groupInfo.groupIdx = :groupIdx " +
            "and a.status = :status ")
    ArrayList<Long> findUsersInGroup(@Param("groupIdx") Long groupIdx, @Param("status") Integer status);


    @Query("select u.fcmtoken " +
            "from User u " +
            "where u.userIdx in :userIdx ")
    ArrayList<String> findUserToken(@Param("userIdx") ArrayList<Long> userIdx);




    @Modifying(clearAutomatically = true)
    @Query(value = "update GROUPINFO g " +
            "set g.status = 1 " +
            "where g.groupStart = :groupStart and g.status = 0 ", nativeQuery = true)
    Integer updateGroupStatusSetStart(@Param("groupStart") String groupStart);


}
