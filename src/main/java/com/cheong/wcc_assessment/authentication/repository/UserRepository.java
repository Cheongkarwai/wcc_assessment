package com.cheong.wcc_assessment.authentication.repository;

import com.cheong.wcc_assessment.authentication.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
