package com.example.demo.src.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostRatingStarReq {

    private Long raterIdx; //평가자
    private List<Start> star;

}
