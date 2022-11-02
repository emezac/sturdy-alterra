package com.sturdy.alterra.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sturdy.alterra.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DungeonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dungeon.class);
        Dungeon dungeon1 = new Dungeon();
        dungeon1.setId(UUID.randomUUID());
        Dungeon dungeon2 = new Dungeon();
        dungeon2.setId(dungeon1.getId());
        assertThat(dungeon1).isEqualTo(dungeon2);
        dungeon2.setId(UUID.randomUUID());
        assertThat(dungeon1).isNotEqualTo(dungeon2);
        dungeon1.setId(null);
        assertThat(dungeon1).isNotEqualTo(dungeon2);
    }
}
