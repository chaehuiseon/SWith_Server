package com.swith.external.firebase.model;



import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmRequest {
    private String token;
    private String title;
    private String body;
}
