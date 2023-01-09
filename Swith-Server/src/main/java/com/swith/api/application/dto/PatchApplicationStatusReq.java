package com.swith.api.application.dto;


import lombok.Data;

@Data
public class PatchApplicationStatusReq {

    private Long applicationIdx;
    private Long adminIdx;
    private Integer statusOfApplication;

}
