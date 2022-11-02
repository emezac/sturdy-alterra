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
 * A Pack.
 */
@Entity
@Table(name = "pack")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pack implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "pack_name", nullable = false)
    private String packName;

    @Column(name = "deck_name")
    private String deckName;

    @Column(name = "config_setup")
    private String configSetup;

    @OneToMany(mappedBy = "pack")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pack" }, allowSetters = true)
    private Set<Card> cards = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "location", "games", "packs" }, allowSetters = true)
    private Player player;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Pack id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPackName() {
        return this.packName;
    }

    public Pack packName(String packName) {
        this.setPackName(packName);
        return this;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getDeckName() {
        return this.deckName;
    }

    public Pack deckName(String deckName) {
        this.setDeckName(deckName);
        return this;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getConfigSetup() {
        return this.configSetup;
    }

    public Pack configSetup(String configSetup) {
        this.setConfigSetup(configSetup);
        return this;
    }

    public void setConfigSetup(String configSetup) {
        this.configSetup = configSetup;
    }

    public Set<Card> getCards() {
        return this.cards;
    }

    public void setCards(Set<Card> cards) {
        if (this.cards != null) {
            this.cards.forEach(i -> i.setPack(null));
        }
        if (cards != null) {
            cards.forEach(i -> i.setPack(this));
        }
        this.cards = cards;
    }

    public Pack cards(Set<Card> cards) {
        this.setCards(cards);
        return this;
    }

    public Pack addCard(Card card) {
        this.cards.add(card);
        card.setPack(this);
        return this;
    }

    public Pack removeCard(Card card) {
        this.cards.remove(card);
        card.setPack(null);
        return this;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Pack player(Player player) {
        this.setPlayer(player);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pack)) {
            return false;
        }
        return id != null && id.equals(((Pack) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pack{" +
            "id=" + getId() +
            ", packName='" + getPackName() + "'" +
            ", deckName='" + getDeckName() + "'" +
            ", configSetup='" + getConfigSetup() + "'" +
            "}";
    }
}
