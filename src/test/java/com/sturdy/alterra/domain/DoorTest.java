package com.sturdy.alterra.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sturdy.alterra.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DoorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Door.class);
        Door door1 = new Door();
        door1.setId(UUID.randomUUID());
        Door door2 = new Door();
        door2.setId(door1.getId());
        assertThat(door1).isEqualTo(door2);
        door2.setId(UUID.randomUUID());
        assertThat(door1).isNotEqualTo(door2);
        door1.setId(null);
        assertThat(door1).isNotEqualTo(door2);
    }
}
