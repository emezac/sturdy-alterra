package com.sturdy.alterra.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sturdy.alterra.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pack.class);
        Pack pack1 = new Pack();
        pack1.setId(UUID.randomUUID());
        Pack pack2 = new Pack();
        pack2.setId(pack1.getId());
        assertThat(pack1).isEqualTo(pack2);
        pack2.setId(UUID.randomUUID());
        assertThat(pack1).isNotEqualTo(pack2);
        pack1.setId(null);
        assertThat(pack1).isNotEqualTo(pack2);
    }
}
