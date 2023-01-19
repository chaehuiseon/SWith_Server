package com.swith.api.application.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetApplicationRes {

    @ApiModelProperty(notes = "지원서 Idx", example = "1")
    private Long applicationIdx;

    @ApiModelProperty(notes = "지원서 내용", example = "안녕하세요 알고리즘 스터디 지원한")
    private String applicationContent;

    @ApiModelProperty(notes = "그룹의 title", example = "알고리즘 스터디")
    private String title;

    @ApiModelProperty(notes = "온라인 여부", example = "0")
    private Integer online;

    @ApiModelProperty(notes = "지역코드 1", example = "1111000000")
    private String regionIdx1;

    @ApiModelProperty(notes = "지역코드 2", example = "1111000000")
    private String regionIdx2;

    @ApiModelProperty(notes = "지원서의 현재상태", example = "0")
    private Integer status;

    @ApiModelProperty(notes = "지원 날짜", example = "")
    private LocalDateTime createdAt;

}
