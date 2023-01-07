package com.swith.src.controller;

import com.swith.global.error.exception.BaseException;
import com.swith.api.common.dto.BaseResponse;
import com.swith.api.dto.request.PatchMemoReq;
import com.swith.api.dto.request.PostMemoReq;
import com.swith.src.service.MemoService;
import com.swith.src.service.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groupinfo/memo")
@Api(tags = {"Swith Memo API"})
public class MemoController {

    private final MemoService memoService;
    private final SessionService sessionService;

    public MemoController(MemoService memoService, SessionService sessionService) {
        this.memoService = memoService;
        this.sessionService = sessionService;
    }

    @ApiOperation("회차탭 - 메모 작성 - P16")
    @PostMapping
    public BaseResponse<Long> postMemo (@RequestBody PostMemoReq postMemoReq) {
        try {
            Long memoIdx = memoService.postMemo(postMemoReq);
            return new BaseResponse<>(memoIdx);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("회차탭 - 메모 변경 - P17")
    @PatchMapping()
    public BaseResponse<Long> patchMemo (@RequestBody PatchMemoReq patchMemoReq) {
        try {
            Long memoIdx = memoService.patchMemo(patchMemoReq);
            return new BaseResponse<>(memoIdx);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
