package com.cheong.wcc_assessment.authentication.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {

    private String accessToken;
}
