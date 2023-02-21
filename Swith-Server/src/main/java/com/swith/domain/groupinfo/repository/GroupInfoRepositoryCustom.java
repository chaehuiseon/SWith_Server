package com.swith.domain.groupinfo.repository;

import com.swith.api.groupinfo.dto.GetGroupInfoSearchReq;
import com.swith.api.groupinfo.dto.GetGroupInfoSearchRes;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface GroupInfoRepositoryCustom {

    Slice<GetGroupInfoSearchRes> searchGroupInfo(GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable);
    JPAQuery<Integer> searchtestGroup(GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable);
}
