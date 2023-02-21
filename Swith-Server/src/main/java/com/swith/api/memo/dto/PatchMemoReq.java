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
public class PatchMemoReq {

    @ApiModelProperty(notes = "메모의 Idx", example = "10")
    private Long memoIdx;

    @ApiModelProperty(notes = "바꿀 메모의 내용", example = "1회차 바뀐메모입니다.")
    private String memoContent;
}
