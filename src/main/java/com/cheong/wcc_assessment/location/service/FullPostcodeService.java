package com.cheong.wcc_assessment.location.service;

import com.cheong.wcc_assessment.location.domain.FullPostcode;
import com.cheong.wcc_assessment.location.dto.FullPostcodeDTO;
import com.cheong.wcc_assessment.location.dto.OutcodeDTO;
import com.cheong.wcc_assessment.location.repository.FullPostcodeRepository;
import jakarta.validation.ValidationException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FullPostcodeService {

    private final FullPostcodeRepository fullPostcodeRepository;

    private final Validator validator;

    public FullPostcodeService(FullPostcodeRepository fullPostcodeRepository,
                               Validator validator){
        this.fullPostcodeRepository = fullPostcodeRepository;
        this.validator = validator;
    }

    @Cacheable(value = "fullpostcode", key = "{#outcode, #incode}")
    public Optional<FullPostcodeDTO> findByOutcodeAndIncode(String outcode, String incode){

        String postcode = String.join(" ", outcode, incode);

        return fullPostcodeRepository.findByPostcode(postcode)
                .map(fullPostcode -> FullPostcodeDTO.builder()
                        .postalCode(fullPostcode.getPostcode())
                        .latitude(fullPostcode.getLatitude())
                        .longitude(fullPostcode.getLongitude())
                        .build());
    }

    @CachePut(value = "fullpostcode", key = "{#fullPostcodeDTO.postalCode.split(' ')[0], #fullPostcodeDTO.postalCode.split(' ')[1]}")
    public FullPostcodeDTO save(FullPostcodeDTO fullPostcodeDTO){

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

    @Caching(put = {
            @CachePut(cacheNames = "fullpostcode", key = "{#result.postalCode.split(' ')[0], #result.postalCode.split(' ')[1]}")
    }, evict = {
            @CacheEvict(cacheNames = "fullpostcode", key = "{#postcode.split(' ')[0], #postcode.split(' ')[1]}")
    })
    public FullPostcodeDTO update(String postcode, FullPostcodeDTO fullPostcodeDTO) {

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

}
