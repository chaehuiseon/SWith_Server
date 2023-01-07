package com.swith.api.dto.request;


import lombok.Data;

@Data
public class PatchApplicationStatusReq {

    private Long applicationIdx;
    private Long adminIdx;
    private Integer statusOfApplication;

}
