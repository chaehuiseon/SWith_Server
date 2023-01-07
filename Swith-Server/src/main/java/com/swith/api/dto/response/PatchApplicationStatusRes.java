package com.swith.api.dto.response;

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

    public PatchApplicationStatusRes(Application application) {
        this.applicationIdx = application.getApplicationIdx();
        this.status = application.getStatus();
    }
}
