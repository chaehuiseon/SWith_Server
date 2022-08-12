package com.example.demo.src.repository;

import com.example.demo.src.dto.PostGroupInfoReq;
import com.example.demo.src.dto.request.GetGroupInfoSearchReq;
import com.example.demo.src.dto.response.GetGroupInfoSearchRes;
//import com.example.demo.src.dto.response.QGetGroupInfoSearchRes;
import com.example.demo.src.entity.GroupInfo;
import com.example.demo.src.entity.Interest;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

import static com.example.demo.src.entity.QApplication.application;
import static com.example.demo.src.entity.QGroupInfo.groupInfo;
import static com.example.demo.src.entity.QInterest.interest;


@Repository
@RequiredArgsConstructor
//GroupInfoRepository
//GroupInfoRepositoryImpl
//GroupInfoRepositoryCustom
public class GroupInfoRepositoryImpl implements GroupInfoRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    //                .select(new QGetGroupInfoSearchRes(
//
//                        groupInfo.groupIdx.as("groupIdx"),
//                        groupInfo.title.as("title"),
//                        groupInfo.groupContent.as("groupContent"),
//                        groupInfo.regionIdx1.as("regionIdx1"),
//                        groupInfo.regionIdx2,
//                        groupInfo.recruitmentEndDate,
//                        groupInfo.memberLimit,
//                        application.count().intValue().as("numOfApplicants"),
//                        groupInfo.createdAt
//                ))


    @Override
    public Slice<GetGroupInfoSearchRes> searchGroupInfo(GetGroupInfoSearchReq searchCond, Pageable pageable) {
        List<GetGroupInfoSearchRes> content =  queryFactory

                .select(Projections.fields(GetGroupInfoSearchRes.class,
                        groupInfo.groupIdx,
                        groupInfo.title,
                        groupInfo.groupContent,
                        groupInfo.regionIdx1,
                        groupInfo.regionIdx2,
                        groupInfo.recruitmentEndDate,
                        groupInfo.memberLimit,
                        application.count().intValue().as("numOfApplicants"),
                        groupInfo.createdAt
                        ))
                .from(groupInfo,application)
                .where(titlecontain(searchCond.getTitle()),
                        regionIn(searchCond.getRegionIdx()),
                        interestEq(searchCond.getInterest1()).or(interestEq(searchCond.getInterest2())),
                        application.status.eq(1),
                        groupInfo.groupIdx.eq(application.groupInfo.groupIdx)
                )
                .groupBy(groupInfo.groupIdx)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()+1) // limit보다 데이터를 1개 더 들고와서, 해당 데이터가 있다면 hasNext 변수에 true를 넣어 알림
                .fetch();
        System.out.println(content);

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);

    }


    private BooleanExpression titlecontain(String title){
        System.out.println(title);
        return hasText(title) ? groupInfo.title.eq(title) :null;

    }

    private BooleanExpression regionIn(Long regionIdx){
        if(regionIdx == null){
            return null;
        }
        return groupInfo.regionIdx1.in(regionIdx).or(groupInfo.regionIdx2.in(regionIdx));

    }

    private BooleanExpression interestEq(Integer i){
        System.out.println("진입");
        if(i == null){
            System.out.println("null이래.interestEq"+i);
            return null;
        }

        System.out.println("뭐가 잡혔다");
        System.out.println("interestEq"+i);
        return groupInfo.interest.interestIdx.eq(i);


    }

    public Slice<Long> searchtestGroup(GetGroupInfoSearchReq searchCond, Pageable pageable){
        //em.flush();
        //em.clear();
        System.out.println("repository => "+searchCond.getTitle());

        List<Long> content = queryFactory.select(groupInfo.groupIdx).from(groupInfo)
                .where(
                        groupInfo.title.eq(searchCond.getTitle())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()+1) // limit보다 데이터를 1개 더 들고와서, 해당 데이터가 있다면 hasNext 변수에 true를 넣어 알림
                .fetch();

        System.out.println(content.toString());

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);


    }


}
