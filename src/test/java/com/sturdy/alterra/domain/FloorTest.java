package com.sturdy.alterra.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sturdy.alterra.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FloorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Floor.class);
        Floor floor1 = new Floor();
        floor1.setId(UUID.randomUUID());
        Floor floor2 = new Floor();
        floor2.setId(floor1.getId());
        assertThat(floor1).isEqualTo(floor2);
        floor2.setId(UUID.randomUUID());
        assertThat(floor1).isNotEqualTo(floor2);
        floor1.setId(null);
        assertThat(floor1).isNotEqualTo(floor2);
    }
}
