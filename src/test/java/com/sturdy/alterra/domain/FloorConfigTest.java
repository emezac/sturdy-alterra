package com.sturdy.alterra.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sturdy.alterra.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FloorConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FloorConfig.class);
        FloorConfig floorConfig1 = new FloorConfig();
        floorConfig1.setId(UUID.randomUUID());
        FloorConfig floorConfig2 = new FloorConfig();
        floorConfig2.setId(floorConfig1.getId());
        assertThat(floorConfig1).isEqualTo(floorConfig2);
        floorConfig2.setId(UUID.randomUUID());
        assertThat(floorConfig1).isNotEqualTo(floorConfig2);
        floorConfig1.setId(null);
        assertThat(floorConfig1).isNotEqualTo(floorConfig2);
    }
}
