package com.cheong.wcc_assessment.location.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {

    private String postalCode;

    private double latitude;

    private double longitude;
}
