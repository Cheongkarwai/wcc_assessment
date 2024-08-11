package com.cheong.wcc_assessment.location.service;

import com.cheong.wcc_assessment.core.util.GeographicCalculator;
import com.cheong.wcc_assessment.location.dto.*;
import com.cheong.wcc_assessment.location.domain.FullPostcode;
import com.cheong.wcc_assessment.location.domain.Outcode;
import com.cheong.wcc_assessment.location.repository.FullPostcodeRepository;
import com.cheong.wcc_assessment.location.repository.OutcodeRepository;
import com.cheong.wcc_assessment.location.validator.LocationValidator;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
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

    private final Validator validator;

    public LocationService(FullPostcodeRepository fullPostcodeRepository,
                           OutcodeRepository outcodeRepository,
                           FullPostcodeService fullPostcodeService,
                           OutcodeService outcodeService,
                           Validator validator) {
        this.fullPostcodeRepository = fullPostcodeRepository;
        this.outcodeRepository = outcodeRepository;
        this.outcodeService = outcodeService;
        this.fullPostcodeService = fullPostcodeService;
        this.validator = validator;
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

    @Caching(put = {
            @CachePut(cacheNames = "outcode", key = "#result.outcode")
    }, evict = @CacheEvict(cacheNames = "outcode", key = "#code"))
    public OutcodeDTO updateOutcode(String code, OutcodeDTO outcodeDTO) {

        Errors errors = validator.validateObject(outcodeDTO);

        if (errors.hasErrors()) {
            throw new ValidationException(String.join(",", errors.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList()));
        }

        Outcode outcode = outcodeRepository.findByOutcode(code)
                .orElseThrow(() -> new NoSuchElementException("Unable to find outcode by given code: " + code));

        outcode.setOutcode(outcodeDTO.getOutcode());
        outcode.setLatitude(outcodeDTO.getLatitude());
        outcode.setLongitude(outcodeDTO.getLongitude());
        outcodeRepository.save(outcode);

        return outcodeDTO;
    }

    @Caching(put = {
            @CachePut(cacheNames = "fullpostcode", key = "{#result.postalCode.split(' ')[0], #result.postalCode.split(' ')[1]}")
    }, evict = {
            @CacheEvict(cacheNames = "fullpostcode", key = "{#postcode.split(' ')[0], #postcode.split(' ')[1]}")
    })
    public FullPostcodeDTO updateFullPostcode(String postcode, FullPostcodeDTO fullPostcodeDTO) {

        Errors errors = validator.validateObject(fullPostcodeDTO);

        if (errors.hasErrors()) {

            throw new ValidationException(String.join(",", errors.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList()));
        }

        FullPostcode fullPostcode = fullPostcodeRepository.findByPostcode(postcode)
                .orElseThrow(() -> new NoSuchElementException("Unable to find full postcode by given code: " + postcode));

        fullPostcode.setPostcode(fullPostcodeDTO.getPostalCode());
        fullPostcode.setLatitude(fullPostcodeDTO.getLatitude());
        fullPostcode.setLongitude(fullPostcodeDTO.getLongitude());
        fullPostcodeRepository.save(fullPostcode);

        return fullPostcodeDTO;
    }


    @CachePut(value = "outcode", key = "#outcodeDTO.outcode")
    public OutcodeDTO saveOutcode(OutcodeDTO outcodeDTO){

        Errors errors = validator.validateObject(outcodeDTO);

        if (errors.hasErrors()) {

            throw new ValidationException(String.join(",", errors.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList()));
        }
        Outcode outcode = new Outcode();
        outcode.setOutcode(outcodeDTO.getOutcode());
        outcode.setLongitude(outcodeDTO.getLongitude());
        outcode.setLatitude(outcodeDTO.getLatitude());
        Outcode savedOutcode = outcodeRepository.save(outcode);

        return OutcodeDTO.builder()
                .outcode(savedOutcode.getOutcode())
                .latitude(savedOutcode.getLatitude())
                .longitude(savedOutcode.getLongitude())
                .build();

    }

    @CachePut(value = "fullpostcode", key = "{#fullPostcodeDTO.postalCode.split(' ')[0], #fullPostcodeDTO.postalCode.split(' ')[1]}")
    public FullPostcodeDTO saveFullPostcode(FullPostcodeDTO fullPostcodeDTO){

        Errors errors = validator.validateObject(fullPostcodeDTO);

        if (errors.hasErrors()) {

            throw new ValidationException(String.join(",", errors.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList()));
        }
        FullPostcode fullPostcode = new FullPostcode();
        fullPostcode.setPostcode(fullPostcodeDTO.getPostalCode());
        fullPostcode.setLatitude(fullPostcodeDTO.getLatitude());
        fullPostcode.setLongitude(fullPostcodeDTO.getLongitude());
        FullPostcode savedFullPostcode = fullPostcodeRepository.save(fullPostcode);

        return FullPostcodeDTO.builder()
                .postalCode(savedFullPostcode.getPostcode())
                .latitude(savedFullPostcode.getLatitude())
                .longitude(savedFullPostcode.getLongitude())
                .build();
    }
}
