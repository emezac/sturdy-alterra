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
 * A Player.
 */
@Entity
@Table(name = "player")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @OneToOne
    @JoinColumn(unique = true)
    private Profile location;

    @OneToMany(mappedBy = "player")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "location", "maps", "player" }, allowSetters = true)
    private Set<Game> games = new HashSet<>();

    @OneToMany(mappedBy = "player")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cards", "player" }, allowSetters = true)
    private Set<Pack> packs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Player id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNickName() {
        return this.nickName;
    }

    public Player nickName(String nickName) {
        this.setNickName(nickName);
        return this;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Profile getLocation() {
        return this.location;
    }

    public void setLocation(Profile profile) {
        this.location = profile;
    }

    public Player location(Profile profile) {
        this.setLocation(profile);
        return this;
    }

    public Set<Game> getGames() {
        return this.games;
    }

    public void setGames(Set<Game> games) {
        if (this.games != null) {
            this.games.forEach(i -> i.setPlayer(null));
        }
        if (games != null) {
            games.forEach(i -> i.setPlayer(this));
        }
        this.games = games;
    }

    public Player games(Set<Game> games) {
        this.setGames(games);
        return this;
    }

    public Player addGame(Game game) {
        this.games.add(game);
        game.setPlayer(this);
        return this;
    }

    public Player removeGame(Game game) {
        this.games.remove(game);
        game.setPlayer(null);
        return this;
    }

    public Set<Pack> getPacks() {
        return this.packs;
    }

    public void setPacks(Set<Pack> packs) {
        if (this.packs != null) {
            this.packs.forEach(i -> i.setPlayer(null));
        }
        if (packs != null) {
            packs.forEach(i -> i.setPlayer(this));
        }
        this.packs = packs;
    }

    public Player packs(Set<Pack> packs) {
        this.setPacks(packs);
        return this;
    }

    public Player addPack(Pack pack) {
        this.packs.add(pack);
        pack.setPlayer(this);
        return this;
    }

    public Player removePack(Pack pack) {
        this.packs.remove(pack);
        pack.setPlayer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }
        return id != null && id.equals(((Player) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", nickName='" + getNickName() + "'" +
            "}";
    }
}
