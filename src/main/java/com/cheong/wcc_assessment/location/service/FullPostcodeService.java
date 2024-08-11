package com.cheong.wcc_assessment.location.service;

import com.cheong.wcc_assessment.location.dto.FullPostcodeDTO;
import com.cheong.wcc_assessment.location.dto.OutcodeDTO;
import com.cheong.wcc_assessment.location.repository.FullPostcodeRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FullPostcodeService {

    private final FullPostcodeRepository fullPostcodeRepository;

    public FullPostcodeService(FullPostcodeRepository fullPostcodeRepository){
        this.fullPostcodeRepository = fullPostcodeRepository;
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
}
