package com.swith.api.application.dto;

import com.swith.domain.application.entity.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PatchApplicationStatusRes {
    private Long applicationIdx;
    private Integer status;

    public static PatchApplicationStatusRes of (Application application) {
        return PatchApplicationStatusRes.builder()
                .applicationIdx(application.getApplicationIdx())
                .status(application.getStatus())
                .build();
    }
}
