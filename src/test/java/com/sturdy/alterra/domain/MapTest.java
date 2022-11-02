package com.sturdy.alterra.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sturdy.alterra.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MapTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Map.class);
        Map map1 = new Map();
        map1.setId(UUID.randomUUID());
        Map map2 = new Map();
        map2.setId(map1.getId());
        assertThat(map1).isEqualTo(map2);
        map2.setId(UUID.randomUUID());
        assertThat(map1).isNotEqualTo(map2);
        map1.setId(null);
        assertThat(map1).isNotEqualTo(map2);
    }
}
