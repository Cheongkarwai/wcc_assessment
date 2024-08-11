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
@Table(name = "outcodepostcodes", indexes = {@Index(name = "outcodepostcodes_index", columnList = "outcode", unique = true)})
public class Outcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 8, nullable = false)
    private String outcode;

    @Column(name = "lat", precision = 10, nullable = false)
    private double latitude;

    @Column(name = "lng", precision = 10, nullable = false)
    private double longitude;
}
