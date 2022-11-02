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
 * A Game.
 */
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "game_name", nullable = false)
    private String gameName;

    @Column(name = "description")
    private String description;

    @Column(name = "moves")
    private String moves;

    @OneToOne
    @JoinColumn(unique = true)
    private GameConfig location;

    @OneToMany(mappedBy = "game")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tasks", "game" }, allowSetters = true)
    private Set<Map> maps = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "location", "games", "packs" }, allowSetters = true)
    private Player player;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Game id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getGameName() {
        return this.gameName;
    }

    public Game gameName(String gameName) {
        this.setGameName(gameName);
        return this;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getDescription() {
        return this.description;
    }

    public Game description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMoves() {
        return this.moves;
    }

    public Game moves(String moves) {
        this.setMoves(moves);
        return this;
    }

    public void setMoves(String moves) {
        this.moves = moves;
    }

    public GameConfig getLocation() {
        return this.location;
    }

    public void setLocation(GameConfig gameConfig) {
        this.location = gameConfig;
    }

    public Game location(GameConfig gameConfig) {
        this.setLocation(gameConfig);
        return this;
    }

    public Set<Map> getMaps() {
        return this.maps;
    }

    public void setMaps(Set<Map> maps) {
        if (this.maps != null) {
            this.maps.forEach(i -> i.setGame(null));
        }
        if (maps != null) {
            maps.forEach(i -> i.setGame(this));
        }
        this.maps = maps;
    }

    public Game maps(Set<Map> maps) {
        this.setMaps(maps);
        return this;
    }

    public Game addMap(Map map) {
        this.maps.add(map);
        map.setGame(this);
        return this;
    }

    public Game removeMap(Map map) {
        this.maps.remove(map);
        map.setGame(null);
        return this;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game player(Player player) {
        this.setPlayer(player);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Game)) {
            return false;
        }
        return id != null && id.equals(((Game) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            ", gameName='" + getGameName() + "'" +
            ", description='" + getDescription() + "'" +
            ", moves='" + getMoves() + "'" +
            "}";
    }
}
