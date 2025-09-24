package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.OwnerService;

@ExtendWith(MockitoExtension.class)
public class OwnerServiceTest {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerService ownerService;

    @Test
    void testGetOwnerCount() {
        when(ownerRepository.count()).thenReturn(5L);

        long count = ownerService.getOwnerCount();

        assertThat(count).isEqualTo(5L);
        verify(ownerRepository, times(1)).count();
    }
}
