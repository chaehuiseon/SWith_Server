package com.swith.src.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostApplicationReq {

    private Long userIdx;

    private String applicationContent;

}
