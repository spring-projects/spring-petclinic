package org.springframework.samples.petclinic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.Assert.*;

public class FailingTest {

    @Test
    public void checkValues(){
        assertThat(true).isEqualTo(false);
    }

}

    
    
