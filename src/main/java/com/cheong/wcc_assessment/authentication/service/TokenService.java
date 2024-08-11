package com.cheong.wcc_assessment.authentication.service;

import com.cheong.wcc_assessment.authentication.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Value("${jwt.issuer}")
    private String issuer;

    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder){
        this.jwtEncoder = jwtEncoder;
    }
    public String generateAccessToken(UserDetails userDetails){
        JwsHeader jwsHeader = JwsHeader.with(() -> JwsAlgorithms.HS256).build();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(userDetails.getUsername())
                .issuer(issuer)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(30, ChronoUnit.MINUTES)).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,claimsSet)).getTokenValue();
    }
}
