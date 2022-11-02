package com.sturdy.alterra.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RoomConfig.
 */
@Entity
@Table(name = "room_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoomConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "setup")
    private String setup;

    @Column(name = "num_of_doors")
    private Long numOfDoors;

    @Column(name = "num_of_prizes")
    private Long numOfPrizes;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public RoomConfig id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSetup() {
        return this.setup;
    }

    public RoomConfig setup(String setup) {
        this.setSetup(setup);
        return this;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public Long getNumOfDoors() {
        return this.numOfDoors;
    }

    public RoomConfig numOfDoors(Long numOfDoors) {
        this.setNumOfDoors(numOfDoors);
        return this;
    }

    public void setNumOfDoors(Long numOfDoors) {
        this.numOfDoors = numOfDoors;
    }

    public Long getNumOfPrizes() {
        return this.numOfPrizes;
    }

    public RoomConfig numOfPrizes(Long numOfPrizes) {
        this.setNumOfPrizes(numOfPrizes);
        return this;
    }

    public void setNumOfPrizes(Long numOfPrizes) {
        this.numOfPrizes = numOfPrizes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoomConfig)) {
            return false;
        }
        return id != null && id.equals(((RoomConfig) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomConfig{" +
            "id=" + getId() +
            ", setup='" + getSetup() + "'" +
            ", numOfDoors=" + getNumOfDoors() +
            ", numOfPrizes=" + getNumOfPrizes() +
            "}";
    }
}
