package com.cheong.wcc_assessment.location.repository;

import com.cheong.wcc_assessment.location.domain.FullPostcode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FullPostcodeRepository extends JpaRepository<FullPostcode, Integer> {

    Optional<FullPostcode> findByPostcode(String postcode);

    boolean existsByPostcode(String postcode);
}
