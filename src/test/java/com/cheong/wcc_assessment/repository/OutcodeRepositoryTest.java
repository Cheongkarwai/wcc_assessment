package com.cheong.wcc_assessment.repository;

import com.cheong.wcc_assessment.location.domain.Outcode;
import com.cheong.wcc_assessment.location.repository.OutcodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class OutcodeRepositoryTest{

    @Autowired
    private OutcodeRepository outcodeRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void givenOutcode_whenSave_thenSuccess(){
        Outcode outcode = new Outcode();
        outcode.setOutcode("AB1");
        outcode.setLongitude(10.00);
        outcode.setLatitude(20.00);
        Outcode savedOutcode = outcodeRepository.save(outcode);
        assertEquals(entityManager.find(Outcode.class, outcode.getId()), savedOutcode);
        assertEquals(outcode.getOutcode(), savedOutcode.getOutcode());
        assertEquals(outcode.getLatitude(), savedOutcode.getLatitude());
        assertEquals(outcode.getLongitude(), savedOutcode.getLongitude());
    }
}
