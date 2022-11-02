package com.sturdy.alterra.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sturdy.alterra.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ChallengeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Challenge.class);
        Challenge challenge1 = new Challenge();
        challenge1.setId(UUID.randomUUID());
        Challenge challenge2 = new Challenge();
        challenge2.setId(challenge1.getId());
        assertThat(challenge1).isEqualTo(challenge2);
        challenge2.setId(UUID.randomUUID());
        assertThat(challenge1).isNotEqualTo(challenge2);
        challenge1.setId(null);
        assertThat(challenge1).isNotEqualTo(challenge2);
    }
}
