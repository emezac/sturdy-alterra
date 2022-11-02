package com.sturdy.alterra.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FloorConfig.
 */
@Entity
@Table(name = "floor_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FloorConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "setup", nullable = false)
    private String setup;

    @Column(name = "num_of_rooms")
    private Long numOfRooms;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public FloorConfig id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSetup() {
        return this.setup;
    }

    public FloorConfig setup(String setup) {
        this.setSetup(setup);
        return this;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public Long getNumOfRooms() {
        return this.numOfRooms;
    }

    public FloorConfig numOfRooms(Long numOfRooms) {
        this.setNumOfRooms(numOfRooms);
        return this;
    }

    public void setNumOfRooms(Long numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FloorConfig)) {
            return false;
        }
        return id != null && id.equals(((FloorConfig) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FloorConfig{" +
            "id=" + getId() +
            ", setup='" + getSetup() + "'" +
            ", numOfRooms=" + getNumOfRooms() +
            "}";
    }
}
