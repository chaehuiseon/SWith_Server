package com.swith.src.repository;

import com.swith.src.dto.request.GetGroupInfoSearchReq;
import com.swith.src.dto.response.GetGroupInfoSearchRes;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface GroupInfoRepositoryCustom {

    Slice<GetGroupInfoSearchRes> searchGroupInfo(GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable);
    JPAQuery<Integer> searchtestGroup(GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable);
}
