package com.swith.api.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostGroupInfoRes {
    @ApiModelProperty(notes = "스터디 개설한 유저의 PK",example = "1")
    private Long groupIdx;
}
