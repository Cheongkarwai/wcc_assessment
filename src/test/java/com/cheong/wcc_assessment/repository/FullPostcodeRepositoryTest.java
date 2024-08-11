package com.cheong.wcc_assessment.repository;

import com.cheong.wcc_assessment.location.domain.FullPostcode;
import com.cheong.wcc_assessment.location.repository.FullPostcodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class FullPostcodeRepositoryTest {

    @Autowired
    private FullPostcodeRepository fullPostcodeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void givenFullPostcode_whenSave_thenSuccess(){
        FullPostcode fullPostcode = new FullPostcode();
        fullPostcode.setPostcode("AB1 AD1");
        fullPostcode.setLatitude(100.00);
        fullPostcode.setLongitude(20.00);
        FullPostcode savedFullPostcode = fullPostcodeRepository.save(fullPostcode);
        assertEquals(entityManager.find(FullPostcode.class, savedFullPostcode.getId()), fullPostcode);
        assertEquals(fullPostcode.getPostcode(), savedFullPostcode.getPostcode());
        assertEquals(fullPostcode.getLatitude(), savedFullPostcode.getLatitude());
        assertEquals(fullPostcode.getLongitude(), savedFullPostcode.getLongitude());
    }
}
