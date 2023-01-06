package com.swith.src.dto.request;

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
