package com.cheong.wcc_assessment.location.repository;

import com.cheong.wcc_assessment.location.domain.Outcode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OutcodeRepository extends JpaRepository<Outcode, Integer> {

    Optional<Outcode> findByOutcode(String code);

    boolean existsByOutcode(String code);
}
