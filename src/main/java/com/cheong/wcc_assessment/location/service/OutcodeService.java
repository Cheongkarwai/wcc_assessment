package com.cheong.wcc_assessment.location.service;

import com.cheong.wcc_assessment.location.dto.OutcodeDTO;
import com.cheong.wcc_assessment.location.repository.OutcodeRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OutcodeService {

    private final OutcodeRepository outcodeRepository;

    public OutcodeService(OutcodeRepository outcodeRepository){
        this.outcodeRepository = outcodeRepository;
    }

    @Cacheable(value = "outcode", key = "#code")
    public Optional<OutcodeDTO> findByCode(String code){
        return outcodeRepository.findByOutcode(code)
                .map(outcode -> OutcodeDTO.builder()
                        .outcode(outcode.getOutcode())
                        .longitude(outcode.getLongitude())
                        .latitude(outcode.getLatitude()).build());
    }
}
