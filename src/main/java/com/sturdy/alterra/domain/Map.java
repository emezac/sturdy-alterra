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
 * A Map.
 */
@Entity
@Table(name = "map")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Map implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "map_name", nullable = false)
    private String mapName;

    @Column(name = "description")
    private String description;

    @ManyToMany
    @JoinTable(name = "rel_map__task", joinColumns = @JoinColumn(name = "map_id"), inverseJoinColumns = @JoinColumn(name = "task_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "floors", "jobs" }, allowSetters = true)
    private Set<Dungeon> tasks = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "location", "maps", "player" }, allowSetters = true)
    private Game game;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Map id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMapName() {
        return this.mapName;
    }

    public Map mapName(String mapName) {
        this.setMapName(mapName);
        return this;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getDescription() {
        return this.description;
    }

    public Map description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Dungeon> getTasks() {
        return this.tasks;
    }

    public void setTasks(Set<Dungeon> dungeons) {
        this.tasks = dungeons;
    }

    public Map tasks(Set<Dungeon> dungeons) {
        this.setTasks(dungeons);
        return this;
    }

    public Map addTask(Dungeon dungeon) {
        this.tasks.add(dungeon);
        dungeon.getJobs().add(this);
        return this;
    }

    public Map removeTask(Dungeon dungeon) {
        this.tasks.remove(dungeon);
        dungeon.getJobs().remove(this);
        return this;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Map game(Game game) {
        this.setGame(game);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        return id != null && id.equals(((Map) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Map{" +
            "id=" + getId() +
            ", mapName='" + getMapName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
