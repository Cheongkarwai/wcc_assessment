package com.cheong.wcc_assessment.api;

import com.cheong.wcc_assessment.location.api.LocationApi;
import com.cheong.wcc_assessment.location.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationApi.class)
public class LocationApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @Test
    void givenMissingParameter_thenReturnBadRequest() throws Exception {
        this.mockMvc.perform(get("/api/locations"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidPostalCode_thenReturnNotFound() throws Exception{
        when(locationService.findDistanceByPostalCodes(anyString(), anyString())).thenThrow(new NoSuchElementException("Resource not found"));

        this.mockMvc.perform(get("/api/locations").queryParam("outcode", anyString())
                        .queryParam("incode", anyString()))
                .andExpect(status().isNotFound());
    }
}
