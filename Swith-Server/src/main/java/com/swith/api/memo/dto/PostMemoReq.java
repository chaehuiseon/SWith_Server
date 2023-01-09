package com.swith.api.memo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostMemoReq {

    @ApiModelProperty(notes = "유저의 Idx", example = "2")
    private Long userIdx;
    @ApiModelProperty(notes = "세션의 Idx", example = "21")
    private Long sessionIdx;

    @ApiModelProperty(notes = "메모의 내용", example = "1회차 메모내용 메모메모")
    private String memoContent;


}
