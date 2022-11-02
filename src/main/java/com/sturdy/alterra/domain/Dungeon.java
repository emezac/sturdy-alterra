package com.sturdy.alterra.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Dungeon.
 */
@Entity
@Table(name = "dungeon")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dungeon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "dungeon_name", nullable = false)
    private String dungeonName;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @OneToMany(mappedBy = "name")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "location", "rooms", "name" }, allowSetters = true)
    private Set<Floor> floors = new HashSet<>();

    @ManyToMany(mappedBy = "tasks")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tasks", "game" }, allowSetters = true)
    private Set<Map> jobs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Dungeon id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDungeonName() {
        return this.dungeonName;
    }

    public Dungeon dungeonName(String dungeonName) {
        this.setDungeonName(dungeonName);
        return this;
    }

    public void setDungeonName(String dungeonName) {
        this.dungeonName = dungeonName;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Dungeon startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Dungeon endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Set<Floor> getFloors() {
        return this.floors;
    }

    public void setFloors(Set<Floor> floors) {
        if (this.floors != null) {
            this.floors.forEach(i -> i.setName(null));
        }
        if (floors != null) {
            floors.forEach(i -> i.setName(this));
        }
        this.floors = floors;
    }

    public Dungeon floors(Set<Floor> floors) {
        this.setFloors(floors);
        return this;
    }

    public Dungeon addFloor(Floor floor) {
        this.floors.add(floor);
        floor.setName(this);
        return this;
    }

    public Dungeon removeFloor(Floor floor) {
        this.floors.remove(floor);
        floor.setName(null);
        return this;
    }

    public Set<Map> getJobs() {
        return this.jobs;
    }

    public void setJobs(Set<Map> maps) {
        if (this.jobs != null) {
            this.jobs.forEach(i -> i.removeTask(this));
        }
        if (maps != null) {
            maps.forEach(i -> i.addTask(this));
        }
        this.jobs = maps;
    }

    public Dungeon jobs(Set<Map> maps) {
        this.setJobs(maps);
        return this;
    }

    public Dungeon addJob(Map map) {
        this.jobs.add(map);
        map.getTasks().add(this);
        return this;
    }

    public Dungeon removeJob(Map map) {
        this.jobs.remove(map);
        map.getTasks().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dungeon)) {
            return false;
        }
        return id != null && id.equals(((Dungeon) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dungeon{" +
            "id=" + getId() +
            ", dungeonName='" + getDungeonName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
