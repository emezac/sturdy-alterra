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
 * A Room.
 */
@Entity
@Table(name = "room")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "intro_text")
    private String introText;

    @NotNull
    @Column(name = "room_name", nullable = false)
    private String roomName;

    @OneToOne
    @JoinColumn(unique = true)
    private RoomConfig location;

    @OneToMany(mappedBy = "name")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "prizes", "name" }, allowSetters = true)
    private Set<Door> doors = new HashSet<>();

    @OneToMany(mappedBy = "name")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "name" }, allowSetters = true)
    private Set<Challenge> challenges = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "location", "rooms", "name" }, allowSetters = true)
    private Floor name;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Room id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIntroText() {
        return this.introText;
    }

    public Room introText(String introText) {
        this.setIntroText(introText);
        return this;
    }

    public void setIntroText(String introText) {
        this.introText = introText;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public Room roomName(String roomName) {
        this.setRoomName(roomName);
        return this;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public RoomConfig getLocation() {
        return this.location;
    }

    public void setLocation(RoomConfig roomConfig) {
        this.location = roomConfig;
    }

    public Room location(RoomConfig roomConfig) {
        this.setLocation(roomConfig);
        return this;
    }

    public Set<Door> getDoors() {
        return this.doors;
    }

    public void setDoors(Set<Door> doors) {
        if (this.doors != null) {
            this.doors.forEach(i -> i.setName(null));
        }
        if (doors != null) {
            doors.forEach(i -> i.setName(this));
        }
        this.doors = doors;
    }

    public Room doors(Set<Door> doors) {
        this.setDoors(doors);
        return this;
    }

    public Room addDoor(Door door) {
        this.doors.add(door);
        door.setName(this);
        return this;
    }

    public Room removeDoor(Door door) {
        this.doors.remove(door);
        door.setName(null);
        return this;
    }

    public Set<Challenge> getChallenges() {
        return this.challenges;
    }

    public void setChallenges(Set<Challenge> challenges) {
        if (this.challenges != null) {
            this.challenges.forEach(i -> i.setName(null));
        }
        if (challenges != null) {
            challenges.forEach(i -> i.setName(this));
        }
        this.challenges = challenges;
    }

    public Room challenges(Set<Challenge> challenges) {
        this.setChallenges(challenges);
        return this;
    }

    public Room addChallenge(Challenge challenge) {
        this.challenges.add(challenge);
        challenge.setName(this);
        return this;
    }

    public Room removeChallenge(Challenge challenge) {
        this.challenges.remove(challenge);
        challenge.setName(null);
        return this;
    }

    public Floor getName() {
        return this.name;
    }

    public void setName(Floor floor) {
        this.name = floor;
    }

    public Room name(Floor floor) {
        this.setName(floor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        return id != null && id.equals(((Room) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Room{" +
            "id=" + getId() +
            ", introText='" + getIntroText() + "'" +
            ", roomName='" + getRoomName() + "'" +
            "}";
    }
}
