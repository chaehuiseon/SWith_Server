package com.example.demo.src.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetGroupInfoSearchRes {

    private Long groupIdx;
    private String title;
    private String groupContent;
    private Long regionIdx1;
    private Long regionIdx2;
    private LocalDate recruitmentEndDate; //마감일
    private Integer memberLimit; //정원(2~10)
    private Integer NumOfApplicants; //지원자수
    private LocalDateTime createdAt; //생성일
//
//    //@QueryProjection
//    public GetGroupInfoSearchRes(Long groupIdx,String title, String groupContent, Long regionIdx1,Long regionIdx2, LocalDate recruitmentEndDate, int memberLimit, int numOfApplicants, LocalDateTime createdAt) {
//        this.groupIdx = groupIdx;
//        this.title = title;
//        this.groupContent = groupContent;
//        this.regionIdx1 = regionIdx1;
//        this.regionIdx2 = regionIdx2;
//        this.recruitmentEndDate = recruitmentEndDate;
//        this.memberLimit = memberLimit;
//        this.NumOfApplicants = numOfApplicants;
//        this.createdAt = createdAt;
//    }
}
