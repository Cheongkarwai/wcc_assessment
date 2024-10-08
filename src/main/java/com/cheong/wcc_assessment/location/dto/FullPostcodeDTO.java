package com.cheong.wcc_assessment.location.dto;

import com.cheong.wcc_assessment.location.validator.ExistingFullPostcode;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FullPostcodeDTO {

    @NotBlank(message = "Postal code must not be empty")
    @ExistingFullPostcode
    @Length(max = 8, message = "Full Postcode maximum character length is 8")
    @Pattern(regexp = "^[A-Za-z]+[0-9]+ [A-Za-z]+[0-9]+$",message = "Postal code must have outcode and incode")
    private String postalCode;

    private double latitude;

    private double longitude;

    @Override
    public String toString(){
        return String.format("Postcode %s , Latitude %s, Longitude %s", postalCode, latitude, longitude);
    }
}
