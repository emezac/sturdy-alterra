package com.sturdy.alterra.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sturdy.alterra.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RoomConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomConfig.class);
        RoomConfig roomConfig1 = new RoomConfig();
        roomConfig1.setId(UUID.randomUUID());
        RoomConfig roomConfig2 = new RoomConfig();
        roomConfig2.setId(roomConfig1.getId());
        assertThat(roomConfig1).isEqualTo(roomConfig2);
        roomConfig2.setId(UUID.randomUUID());
        assertThat(roomConfig1).isNotEqualTo(roomConfig2);
        roomConfig1.setId(null);
        assertThat(roomConfig1).isNotEqualTo(roomConfig2);
    }
}
