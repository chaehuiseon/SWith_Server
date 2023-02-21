package com.swith.api.application.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PatchExpelUserReq {
    @NotNull(message = "추방을 하기 위해서, 그룹장의 adminIdx 필요")
    private Long adminIdx;
    @NotNull(message = "추방 할 유저의 userIdx 필요")
    private Long userIdx;
    @NotNull(message = "추방을 위해, applicationIdx 필요")
    private Long applicationIdx;
}
