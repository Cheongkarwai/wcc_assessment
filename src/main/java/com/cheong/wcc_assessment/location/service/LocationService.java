package com.cheong.wcc_assessment.location.service;

import com.cheong.wcc_assessment.core.util.GeographicCalculator;
import com.cheong.wcc_assessment.location.dto.*;
import com.cheong.wcc_assessment.location.repository.FullPostcodeRepository;
import com.cheong.wcc_assessment.location.repository.OutcodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class LocationService {
    private final OutcodeRepository outcodeRepository;

    private final FullPostcodeRepository fullPostcodeRepository;

    private final FullPostcodeService fullPostcodeService;

    private final OutcodeService outcodeService;

    public LocationService(FullPostcodeRepository fullPostcodeRepository,
                           OutcodeRepository outcodeRepository,
                           FullPostcodeService fullPostcodeService,
                           OutcodeService outcodeService) {
        this.fullPostcodeRepository = fullPostcodeRepository;
        this.outcodeRepository = outcodeRepository;
        this.outcodeService = outcodeService;
        this.fullPostcodeService = fullPostcodeService;
    }


    public DistanceDTO findDistanceByPostalCodes(String outcode, String incode) {

        String postcode = String.join(" ", outcode, incode);

        Optional<OutcodeDTO> outcodeOptional = outcodeService.findByCode(outcode)
                .stream()
                .findFirst();

        Optional<FullPostcodeDTO> fullPostcodeOptional = fullPostcodeService.findByOutcodeAndIncode(outcode, incode)
                .stream()
                .findFirst();

        //Throw an exception when no record found using given postcode
        if (fullPostcodeOptional.isEmpty()) {
            throw new NoSuchElementException("Unable to find latitude and longtitude for full postcode: " + postcode);
        }

        if (outcodeOptional.isEmpty()) {
            throw new NoSuchElementException("Unable to find latitude and longtitude for outward code: " + outcode);
        }
        OutcodeDTO outwardCode = outcodeOptional.get();
        FullPostcodeDTO fullPostcode = fullPostcodeOptional.get();

        double distance = GeographicCalculator.calculateDistance(
                outwardCode.getLatitude(),
                outwardCode.getLongitude(),
                fullPostcode.getLatitude(),
                fullPostcode.getLongitude());

        LocationDTO firstLocation = LocationDTO.builder()
                .postalCode(outwardCode.getOutcode())
                .longitude(outwardCode.getLongitude())
                .latitude(outwardCode.getLatitude())
                .build();

        LocationDTO secondLocation = LocationDTO.builder()
                .postalCode(fullPostcode.getPostalCode()
                        .split(" ")[1])
                .longitude(fullPostcode.getLongitude())
                .latitude(fullPostcode.getLatitude())
                .build();

        return DistanceDTO.builder()
                .locations(List.of(firstLocation, secondLocation))
                .distance(distance)
                .unit(Unit.km.name())
                .build();
    }
}
