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
 * A Floor.
 */
@Entity
@Table(name = "floor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Floor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "floor_name", nullable = false)
    private String floorName;

    @OneToOne
    @JoinColumn(unique = true)
    private FloorConfig location;

    @OneToMany(mappedBy = "name")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "location", "doors", "challenges", "name" }, allowSetters = true)
    private Set<Room> rooms = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "floors", "jobs" }, allowSetters = true)
    private Dungeon name;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Floor id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFloorName() {
        return this.floorName;
    }

    public Floor floorName(String floorName) {
        this.setFloorName(floorName);
        return this;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public FloorConfig getLocation() {
        return this.location;
    }

    public void setLocation(FloorConfig floorConfig) {
        this.location = floorConfig;
    }

    public Floor location(FloorConfig floorConfig) {
        this.setLocation(floorConfig);
        return this;
    }

    public Set<Room> getRooms() {
        return this.rooms;
    }

    public void setRooms(Set<Room> rooms) {
        if (this.rooms != null) {
            this.rooms.forEach(i -> i.setName(null));
        }
        if (rooms != null) {
            rooms.forEach(i -> i.setName(this));
        }
        this.rooms = rooms;
    }

    public Floor rooms(Set<Room> rooms) {
        this.setRooms(rooms);
        return this;
    }

    public Floor addRoom(Room room) {
        this.rooms.add(room);
        room.setName(this);
        return this;
    }

    public Floor removeRoom(Room room) {
        this.rooms.remove(room);
        room.setName(null);
        return this;
    }

    public Dungeon getName() {
        return this.name;
    }

    public void setName(Dungeon dungeon) {
        this.name = dungeon;
    }

    public Floor name(Dungeon dungeon) {
        this.setName(dungeon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Floor)) {
            return false;
        }
        return id != null && id.equals(((Floor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Floor{" +
            "id=" + getId() +
            ", floorName='" + getFloorName() + "'" +
            "}";
    }
}
