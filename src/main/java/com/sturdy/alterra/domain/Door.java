package com.sturdy.alterra.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Door.
 */
@Entity
@Table(name = "door")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Door implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "door_name", nullable = false)
    private String doorName;

    @OneToMany(mappedBy = "name")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "name" }, allowSetters = true)
    private Set<Prize> prizes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "location", "doors", "challenges", "name" }, allowSetters = true)
    private Room name;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Door id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDoorName() {
        return this.doorName;
    }

    public Door doorName(String doorName) {
        this.setDoorName(doorName);
        return this;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }

    public Set<Prize> getPrizes() {
        return this.prizes;
    }

    public void setPrizes(Set<Prize> prizes) {
        if (this.prizes != null) {
            this.prizes.forEach(i -> i.setName(null));
        }
        if (prizes != null) {
            prizes.forEach(i -> i.setName(this));
        }
        this.prizes = prizes;
    }

    public Door prizes(Set<Prize> prizes) {
        this.setPrizes(prizes);
        return this;
    }

    public Door addPrize(Prize prize) {
        this.prizes.add(prize);
        prize.setName(this);
        return this;
    }

    public Door removePrize(Prize prize) {
        this.prizes.remove(prize);
        prize.setName(null);
        return this;
    }

    public Room getName() {
        return this.name;
    }

    public void setName(Room room) {
        this.name = room;
    }

    public Door name(Room room) {
        this.setName(room);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Door)) {
            return false;
        }
        return id != null && id.equals(((Door) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Door{" +
            "id=" + getId() +
            ", doorName='" + getDoorName() + "'" +
            "}";
    }
}
