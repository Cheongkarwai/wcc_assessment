package com.cheong.wcc_assessment.location.api;

import com.cheong.wcc_assessment.location.dto.DistanceDTO;
import com.cheong.wcc_assessment.location.dto.FullPostcodeDTO;
import com.cheong.wcc_assessment.location.dto.OutcodeDTO;
import com.cheong.wcc_assessment.location.service.FullPostcodeService;
import com.cheong.wcc_assessment.location.service.LocationService;
import com.cheong.wcc_assessment.location.service.OutcodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/locations")
@Slf4j
public class LocationApi {

    private final LocationService locationService;

    private final FullPostcodeService fullPostcodeService;

    private final OutcodeService outcodeService;

    public LocationApi(LocationService locationService,
                       FullPostcodeService fullPostcodeService,
                       OutcodeService outcodeService){
        this.locationService = locationService;
        this.fullPostcodeService = fullPostcodeService;
        this.outcodeService = outcodeService;
    }


    @GetMapping
    public HttpEntity<DistanceDTO> findDistanceByPostalCodes(@RequestParam("outcode") String outcode,
                                                         @RequestParam("incode") String incode){

        log.info("Request parameter outcode : {}", outcode);
        log.info("Request parameter incode : {},", incode);

        return ResponseEntity.ok(locationService.findDistanceByPostalCodes(outcode, incode));
    }

    @PostMapping("/outcodes")
    public HttpEntity<Void> saveOutcode(@RequestBody OutcodeDTO outcodeDTO){

        log.info("Request Body OutcodeDTO : {}", outcodeDTO.toString());

        OutcodeDTO savedOutcode = outcodeService.save(outcodeDTO);
        URI uri = UriComponentsBuilder.newInstance()
                .path("/api/locations/outcodes")
                .pathSegment("{code}")
                .build(outcodeDTO.getOutcode());
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/fullpostcodes")
    public HttpEntity<Void> savePostcode(@RequestBody FullPostcodeDTO fullPostcodeDTO){

        log.info("Request Body FullPostcodeDTO : {}", fullPostcodeDTO.toString());

        FullPostcodeDTO savedFullPostcode = fullPostcodeService.save(fullPostcodeDTO);
        URI uri = UriComponentsBuilder.newInstance()
                .path("/api/locations/fullpostcodes")
                .pathSegment("{code}")
                .build(fullPostcodeDTO.getPostalCode());
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/outcodes/{outcode}")
    public HttpEntity<OutcodeDTO> updateOutcode(@PathVariable String outcode, @RequestBody OutcodeDTO outcodeDTO){

        log.info("Path variables outcode: {}", outcode);
        log.info("Request Body OutcodeDTO : {}", outcodeDTO.toString());

        return ResponseEntity.ok(outcodeService.update(outcode, outcodeDTO));
    }

    @PutMapping("/fullpostcodes/{postcode}")
    public HttpEntity<FullPostcodeDTO> updateFullPostcode(@PathVariable String postcode, @RequestBody FullPostcodeDTO fullPostcodeDTO){

        log.info("Path variables outcode: {}", postcode);
        log.info("Request Body OutcodeDTO : {}", fullPostcodeDTO.toString());

        return ResponseEntity.ok(fullPostcodeService.update(postcode, fullPostcodeDTO));
    }
}
