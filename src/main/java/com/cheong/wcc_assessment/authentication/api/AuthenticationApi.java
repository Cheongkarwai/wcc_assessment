package com.cheong.wcc_assessment.authentication.api;

import com.cheong.wcc_assessment.authentication.dto.LoginDTO;
import com.cheong.wcc_assessment.authentication.dto.TokenDTO;
import com.cheong.wcc_assessment.authentication.service.TokenService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationApi {


    private final DaoAuthenticationProvider daoAuthenticationProvider;

    private final TokenService tokenService;

    public AuthenticationApi(DaoAuthenticationProvider daoAuthenticationProvider,
                             TokenService tokenService) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public HttpEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(TokenDTO.builder()
                .accessToken(tokenService.generateAccessToken(userDetails))
                .build());
    }
}
