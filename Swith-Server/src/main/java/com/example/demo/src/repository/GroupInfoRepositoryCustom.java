package com.example.demo.src.repository;

import com.example.demo.src.dto.PostGroupInfoReq;
import com.example.demo.src.dto.request.GetGroupInfoSearchReq;
import com.example.demo.src.dto.response.GetGroupInfoSearchRes;
import com.example.demo.src.entity.GroupInfo;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface GroupInfoRepositoryCustom {

    Slice<GetGroupInfoSearchRes> searchGroupInfo(GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable);
    JPAQuery<Integer> searchtestGroup(GetGroupInfoSearchReq getGroupInfoSearchReq, Pageable pageable);
}
