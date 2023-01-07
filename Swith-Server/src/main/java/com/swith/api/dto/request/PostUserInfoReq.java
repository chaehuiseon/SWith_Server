package com.swith.api.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostUserInfoReq {
    @ApiModelProperty(notes = "유저 고유 idx",example = "53")
    private Long userIdx;

}
