package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class OwnerRepositoryTest {

    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    void testCountOwners() {
        long count = ownerRepository.count();
        assertThat(count).isGreaterThan(0);  // データがあることを確認
    }
}
