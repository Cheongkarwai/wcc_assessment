package com.cheong.wcc_assessment.location.dto;

import com.cheong.wcc_assessment.location.validator.ExistingOutcode;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutcodeDTO {

    @NotBlank(message = "Outcode must not be empty")
    @ExistingOutcode
    private String outcode;

    private double latitude;

    private double longitude;

    @Override
    public String toString(){
        return String.format("Outcode %s , Latitude %s, Longitude %s", outcode, latitude, longitude);
    }
}
