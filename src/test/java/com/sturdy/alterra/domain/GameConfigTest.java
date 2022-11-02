package com.sturdy.alterra.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sturdy.alterra.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class GameConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameConfig.class);
        GameConfig gameConfig1 = new GameConfig();
        gameConfig1.setId(UUID.randomUUID());
        GameConfig gameConfig2 = new GameConfig();
        gameConfig2.setId(gameConfig1.getId());
        assertThat(gameConfig1).isEqualTo(gameConfig2);
        gameConfig2.setId(UUID.randomUUID());
        assertThat(gameConfig1).isNotEqualTo(gameConfig2);
        gameConfig1.setId(null);
        assertThat(gameConfig1).isNotEqualTo(gameConfig2);
    }
}
