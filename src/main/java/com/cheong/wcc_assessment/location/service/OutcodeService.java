package com.cheong.wcc_assessment.location.service;

import com.cheong.wcc_assessment.location.domain.Outcode;
import com.cheong.wcc_assessment.location.dto.OutcodeDTO;
import com.cheong.wcc_assessment.location.repository.OutcodeRepository;
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

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class OutcodeService {

    private final OutcodeRepository outcodeRepository;

    private final Validator validator;

    public OutcodeService(OutcodeRepository outcodeRepository,
                          Validator validator){
        this.outcodeRepository = outcodeRepository;
        this.validator = validator;
    }

    @Cacheable(value = "outcode", key = "#code")
    public Optional<OutcodeDTO> findByCode(String code){
        return outcodeRepository.findByOutcode(code)
                .map(outcode -> OutcodeDTO.builder()
                        .outcode(outcode.getOutcode())
                        .longitude(outcode.getLongitude())
                        .latitude(outcode.getLatitude()).build());
    }

    @CachePut(value = "outcode", key = "#outcodeDTO.outcode")
    public OutcodeDTO save(OutcodeDTO outcodeDTO){

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

    @Caching(put = {
            @CachePut(cacheNames = "outcode", key = "#result.outcode")
    }, evict = @CacheEvict(cacheNames = "outcode", key = "#code"))
    public OutcodeDTO update(String code, OutcodeDTO outcodeDTO) {

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
}
