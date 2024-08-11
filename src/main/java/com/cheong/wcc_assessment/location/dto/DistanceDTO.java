package com.cheong.wcc_assessment.location.dto;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DistanceDTO {

    private List<LocationDTO> locations = new ArrayList<>();

    private double distance;

    private String unit;
}
