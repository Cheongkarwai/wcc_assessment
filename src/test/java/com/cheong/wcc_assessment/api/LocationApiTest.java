package com.cheong.wcc_assessment.api;

import com.cheong.wcc_assessment.location.api.LocationApi;
import com.cheong.wcc_assessment.location.domain.FullPostcode;
import com.cheong.wcc_assessment.location.domain.Outcode;
import com.cheong.wcc_assessment.location.dto.FullPostcodeDTO;
import com.cheong.wcc_assessment.location.dto.OutcodeDTO;
import com.cheong.wcc_assessment.location.service.FullPostcodeService;
import com.cheong.wcc_assessment.location.service.LocationService;
import com.cheong.wcc_assessment.location.service.OutcodeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationApi.class)
public class LocationApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @MockBean
    private OutcodeService outcodeService;

    @MockBean
    private FullPostcodeService fullPostcodeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenMissingParameter_thenReturnBadRequest() throws Exception {
        this.mockMvc.perform(get("/api/locations/distance")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidPostalCode_thenReturnNotFound() throws Exception {
        when(locationService.findDistanceByPostalCodes(anyString(), anyString())).thenThrow(new NoSuchElementException("Resource not found"));

        this.mockMvc.perform(get("/api/locations/distance").with(SecurityMockMvcRequestPostProcessors.jwt()).queryParam("outcode", anyString())
                        .queryParam("incode", anyString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveFullPostcode_thenReturnCreated() throws Exception {

        FullPostcode fullPostcode = new FullPostcode();
        fullPostcode.setPostcode("AB1 A2");
        fullPostcode.setLatitude(20.00);
        fullPostcode.setLongitude(10.00);

        this.mockMvc.perform(post("/api/locations/fullpostcodes").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(fullPostcode)))
                .andExpect(status().isCreated());
    }

    @Test
    void saveOutcode_thenReturnCreated() throws Exception{
        Outcode outcode = new Outcode();
        outcode.setOutcode("A4");
        outcode.setLatitude(20.00);
        outcode.setLongitude(10.00);

        this.mockMvc.perform(post("/api/locations/outcodes").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(outcode)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateFullPostcodeWithInvalidPostalCode_thenReturnNotFound() throws  Exception{

        FullPostcodeDTO fullPostcode = new FullPostcodeDTO();
        fullPostcode.setPostalCode("Test");
        fullPostcode.setLatitude(20.00);
        fullPostcode.setLongitude(10.00);

        when(fullPostcodeService.update(anyString(), any())).thenThrow(new NoSuchElementException("Unable to find full postcode by given code: " + fullPostcode.getPostalCode()));

        this.mockMvc.perform(put("/api/locations/fullpostcodes/"+fullPostcode.getPostalCode()).with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(fullPostcode)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateOutcodecodeWithInvalidPostalCode_thenReturnNotFound() throws  Exception{

        OutcodeDTO fullPostcode = new OutcodeDTO();
        fullPostcode.setOutcode("Test");
        fullPostcode.setLatitude(20.00);
        fullPostcode.setLongitude(10.00);

        when(outcodeService.update(anyString(), any())).thenThrow(new NoSuchElementException("Unable to find outcode by given code: " + fullPostcode.getOutcode()));

        this.mockMvc.perform(put("/api/locations/outcodes/"+fullPostcode.getOutcode()).with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(fullPostcode)))
                .andExpect(status().isNotFound());
    }
}
