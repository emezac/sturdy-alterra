package com.sturdy.alterra.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GameConfig.
 */
@Entity
@Table(name = "game_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "setup_date")
    private Instant setupDate;

    @Column(name = "floor_config")
    private Long floorConfig;

    @Column(name = "room_config")
    private Long roomConfig;

    @Column(name = "date_init")
    private Instant dateInit;

    @Column(name = "date_end")
    private Instant dateEnd;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public GameConfig id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getSetupDate() {
        return this.setupDate;
    }

    public GameConfig setupDate(Instant setupDate) {
        this.setSetupDate(setupDate);
        return this;
    }

    public void setSetupDate(Instant setupDate) {
        this.setupDate = setupDate;
    }

    public Long getFloorConfig() {
        return this.floorConfig;
    }

    public GameConfig floorConfig(Long floorConfig) {
        this.setFloorConfig(floorConfig);
        return this;
    }

    public void setFloorConfig(Long floorConfig) {
        this.floorConfig = floorConfig;
    }

    public Long getRoomConfig() {
        return this.roomConfig;
    }

    public GameConfig roomConfig(Long roomConfig) {
        this.setRoomConfig(roomConfig);
        return this;
    }

    public void setRoomConfig(Long roomConfig) {
        this.roomConfig = roomConfig;
    }

    public Instant getDateInit() {
        return this.dateInit;
    }

    public GameConfig dateInit(Instant dateInit) {
        this.setDateInit(dateInit);
        return this;
    }

    public void setDateInit(Instant dateInit) {
        this.dateInit = dateInit;
    }

    public Instant getDateEnd() {
        return this.dateEnd;
    }

    public GameConfig dateEnd(Instant dateEnd) {
        this.setDateEnd(dateEnd);
        return this;
    }

    public void setDateEnd(Instant dateEnd) {
        this.dateEnd = dateEnd;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameConfig)) {
            return false;
        }
        return id != null && id.equals(((GameConfig) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameConfig{" +
            "id=" + getId() +
            ", setupDate='" + getSetupDate() + "'" +
            ", floorConfig=" + getFloorConfig() +
            ", roomConfig=" + getRoomConfig() +
            ", dateInit='" + getDateInit() + "'" +
            ", dateEnd='" + getDateEnd() + "'" +
            "}";
    }
}
