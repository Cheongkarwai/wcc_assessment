package com.cheong.wcc_assessment.location.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "postcodelatlng", indexes = {@Index(name = "postcodelatlng_index", columnList = "postcode", unique = true)})
public class FullPostcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 8, nullable = false)
    private String postcode;

    @Column(precision = 10, nullable = false)
    private double latitude;

    @Column(precision = 10, nullable = false)
    private double longitude;
}
