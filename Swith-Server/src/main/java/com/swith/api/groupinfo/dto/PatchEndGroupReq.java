package com.swith.api.groupinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PatchEndGroupReq {
    private Long adminIdx;
    private Long groupIdx;
}
