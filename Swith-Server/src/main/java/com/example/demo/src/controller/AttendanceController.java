package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.dto.request.PatchAttendanceReq;
import com.example.demo.src.dto.response.GetGroupAttendanceRes;
import com.example.demo.src.dto.response.GetSessionAttendanceRes;
import com.example.demo.src.dto.response.GetSessionRes;
import com.example.demo.src.repository.AttendanceRepository;
import com.example.demo.src.service.AttendanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = {"Swith Attendance API"})
@RequestMapping("/groupinfo/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @ApiOperation("스터디탭 - 출석 현황 조회 - P9")
    @GetMapping()
    public BaseResponse<GetGroupAttendanceRes> getGroupAttendance(@RequestParam Long groupIdx) {
        try {
            GetGroupAttendanceRes getGroupAttendanceRes = attendanceService.getGroupAttendance(groupIdx);
            return new BaseResponse<>(getGroupAttendanceRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("스터디탭 - 사용자 출석 - P11")
    @PatchMapping()
    public BaseResponse<Long> Attend(@RequestParam(value = "userIdx") Long userIdx,
                                     @RequestParam(value = "sessionIdx") Long sessionIdx) {
        try {
            Long attendanceIdx = attendanceService.updateAttendance(userIdx, sessionIdx);
            return new BaseResponse<>(attendanceIdx);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("관리자탭 - 출석 정보 변경 - P12")
    @PatchMapping("/admin/status")
    public BaseResponse<Integer> modifyAttendance(@RequestBody List<PatchAttendanceReq> patchAttendanceReqList) {
        try {
            Integer numberOfModified = attendanceService.modifyAttendance(patchAttendanceReqList);
            return new BaseResponse<>(numberOfModified);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("관리자탭 - 출석 정보 조회 - P15")
    @PatchMapping("/admin")
    public BaseResponse<List<GetSessionAttendanceRes>> getSessionAttendance(@RequestParam Long groupIdx) {
        try {
            List<GetSessionAttendanceRes> getSessionAttendanceResList = attendanceService.getSessionAttendance(groupIdx);
            return new BaseResponse<>(getSessionAttendanceResList);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

}
