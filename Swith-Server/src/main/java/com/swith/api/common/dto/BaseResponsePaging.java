package com.swith.api.common.dto;

import com.swith.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result", "pageInfo"})
public class BaseResponsePaging<T> {
    @JsonProperty("isSuccess")
    private Boolean isSuccess;
    private String message;
    private int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;
    private PagingRes pageInfo;

    // 페이징이 필요한 요청에 성공한 경우
    public BaseResponsePaging(T result, PagingRes pageInfo) {
        this.message = ErrorCode.SUCCESS.getMessage();
        this.code = ErrorCode.SUCCESS.getCode();
        this.result = result;
        this.pageInfo = pageInfo;
    }

    // 요청에 실패한 경우
    public BaseResponsePaging(ErrorCode status) {
        this.message = status.getMessage();
        this.code = status.getCode();
    }

}