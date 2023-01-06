package com.swith.src.repository;

import com.swith.src.dto.request.GetGroupInfoSearchReq;
import com.swith.src.dto.response.GetGroupInfoSearchRes;
//import com.example.demo.src.dto.response.QGetGroupInfoSearchRes;
import com.swith.src.entity.QGroupInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swith.src.entity.QApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;


@Repository
@RequiredArgsConstructor
//GroupInfoRepository
//GroupInfoRepositoryImpl
//GroupInfoRepositoryCustom
public class GroupInfoRepositoryImpl implements GroupInfoRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    QGroupInfo groupInfoSub = new QGroupInfo("groupInfoSub");
    @Override
    public Slice<GetGroupInfoSearchRes> searchGroupInfo(GetGroupInfoSearchReq searchCond, Pageable pageable) {

        if(searchCond.getGroupIdx()!= null){
            System.out.println("groupIdx >> "+searchCond.getGroupIdx());
        }
        //커버링 인덱스 전략
        List<Long> ids = queryFactory
                .select(QGroupInfo.groupInfo.groupIdx)
                .from(QGroupInfo.groupInfo)
                .where(Read(searchCond.getSortCond(),
                        searchCond.getGroupIdx(),
                        searchCond.getClientTime()))
                .fetch();

        System.out.println("PK >> " + ids.toString());

        List<GetGroupInfoSearchRes> content =
                queryFactory
                        .select(Projections.fields(GetGroupInfoSearchRes.class,
                                QGroupInfo.groupInfo.groupIdx,
                                QGroupInfo.groupInfo.title,
                                QGroupInfo.groupInfo.groupContent,
                                QGroupInfo.groupInfo.regionIdx1,
                                QGroupInfo.groupInfo.regionIdx2,
                                QGroupInfo.groupInfo.recruitmentEndDate,
                                QGroupInfo.groupInfo.memberLimit,
                                QGroupInfo.groupInfo.createdAt,
                                QGroupInfo.groupInfo.applicationMethod,
                                ExpressionUtils.as(
                                        JPAExpressions.select(QApplication.application.count()).from(QApplication.application)
                                                .where(QApplication.application.groupInfo.groupIdx.eq(QGroupInfo.groupInfo.groupIdx),
                                                        QApplication.application.status.eq(1)),"NumOfApplicants")
                        ))
                        .from(QGroupInfo.groupInfo)
                        .where(
                                QGroupInfo.groupInfo.groupIdx.in(ids),
                                Search(searchCond)
                        )

                        .groupBy(QGroupInfo.groupInfo.groupIdx)
                        .limit(pageable.getPageSize()+1) // limit보다 데이터를 1개 더 들고와서, 해당 데이터가 있다면 hasNext 변수에 true를 넣어 알림
                        .orderBy((OrderSpecifier<?>) Sort(searchCond.getSortCond()))
                        .fetch();
        //.orderBy(new OrderSpecifier(Order.ASC,groupInfo.recruitmentEndDate)
        System.out.println(content.toString());
        System.out.println("content.size>>>>>" + content.size());
        System.out.println("getPageaSize>>>>" + pageable.getPageSize());


        boolean hasNext = false;

        if (content.size() > pageable.getPageSize()) {
            System.out.println("들어와????? true로 바뀌냐?");
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);

    }



    private BooleanBuilder Search(GetGroupInfoSearchReq searchCond){
        BooleanBuilder builder = new BooleanBuilder();
        BooleanBuilder builder2 = new BooleanBuilder(); // and ( or ) and // or 괄호용
        builder.and(titlecontain(searchCond.getTitle()));
        if(searchCond.getRegionIdx() != null){

            //builder.or(groupInfo.regionIdx1.eq(searchCond.getRegionIdx()));
            //builder.or(groupInfo.regionIdx2.eq(searchCond.getRegionIdx()));
            //regionIdx string으로 바뀌면 startWith 쓰먼됨.
            builder2.or(QGroupInfo.groupInfo.regionIdx1.startsWith(searchCond.getRegionIdx()));
            builder2.or(QGroupInfo.groupInfo.regionIdx2.startsWith(searchCond.getRegionIdx()));
            builder.and(builder2);
        }
        //builder.or(interestEq(searchCond.getInterest1()));
        //builder.or(interestEq(searchCond.getInterest2()));

        System.out.println( "interset >> " +searchCond.getInterest1()+ searchCond.getInterest2());
        builder.and(interestIn(searchCond.getInterest1(), searchCond.getInterest2()));
        return builder;

    }


    private BooleanExpression titlecontain(String title){
        System.out.println(title);
        return hasText(title) ? QGroupInfo.groupInfo.title.contains(title) :null;

    }

//    private BooleanExpression regionIn(Long regionIdx){
//        if(regionIdx == null){
//            return null;
//        }
//        return groupInfo.regionIdx1.in(regionIdx).or(groupInfo.regionIdx2.in(regionIdx));
//
//    }

//    private BooleanExpression interestEq(Integer i){
//        System.out.println("진입");
//        if(i == null){
//            System.out.println("null이래.interestEq"+i);
//            return null;
//        }
//
//        System.out.println("뭐가 잡혔다");
//        System.out.println("interestEq"+i);
//        return groupInfo.interest.interestIdx.eq(i);
//
//
//    }

    private BooleanExpression interestIn(Integer interest1, Integer interest2){
        if(interest1 != null || interest2 != null){
            return QGroupInfo.groupInfo.interest.interestIdx.in(interest1,interest2);
        }
        return null;
    }


    private BooleanExpression Read(Integer sortCond, Long groupIdx, LocalDateTime now){

        if(groupIdx != null){
            if(sortCond == 0){ //마감일
                System.out.println("마감일 정렬 >> groupIdx >> ");
                return (QGroupInfo.groupInfo.recruitmentEndDate.goe(
                        JPAExpressions.select(groupInfoSub.recruitmentEndDate)
                                .from(groupInfoSub)
                                .where(groupInfoSub.groupIdx.eq(groupIdx))))
                        .and(QGroupInfo.groupInfo.groupIdx.notIn(groupIdx));
            }
            return QGroupInfo.groupInfo.createdAt.lt(
                    JPAExpressions.select(groupInfoSub.createdAt)
                            .from(groupInfoSub)
                            .where(groupInfoSub.groupIdx.eq(groupIdx)));
        }
        //첫 번째 페이지
        System.out.println("첫 페이지 ");
        System.out.println(now);
        System.out.println(LocalDate.from(now));
        if(sortCond == 0 && groupIdx == null){
            System.out.println("마감순");
            return QGroupInfo.groupInfo.recruitmentEndDate.gt(LocalDate.from(now));
        }
        now = now.plusSeconds(1); //ms까지 비교를 못해서 누락되는 데이터 발생 +1s, 0ms로 해서 누락 방지.
        return QGroupInfo.groupInfo.createdAt.lt(now);

    }

    private Object Sort(Integer sortCond){
        if(sortCond == 0){
            return QGroupInfo.groupInfo.recruitmentEndDate.asc();
        }
        return QGroupInfo.groupInfo.createdAt.desc();
    }

//    private BooleanBuilder recruitmentEndSort(Long groupIdx){
//        BooleanBuilder builder1 = new BooleanBuilder();
//        BooleanBuilder builder2 = new BooleanBuilder();
//
//        builder1.and(groupInfo.recruitmentEndDate.goe(
//                JPAExpressions.select(groupInfoSub.recruitmentEndDate)
//                        .from(groupInfoSub)
//                        .where(groupInfoSub.groupIdx.eq(groupIdx))));
//
//        builder2.and()
//
//        builder1.and(builder2);
//    }

    public JPAQuery<Integer> searchtestGroup(GetGroupInfoSearchReq searchCond, Pageable pageable){

        JPAQuery<Integer> numOfApplicants = queryFactory
                .select(QApplication.application.status.count().intValue())
                .from(QApplication.application)
                .where( QApplication.application.groupInfo.groupIdx.eq(searchCond.getGroupIdx()),
                        QApplication.application.status.eq(1));
        return numOfApplicants;


    }


}
