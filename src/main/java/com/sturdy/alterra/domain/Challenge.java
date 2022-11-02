package com.sturdy.alterra.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sturdy.alterra.domain.enumeration.Difficulty;
import com.sturdy.alterra.domain.enumeration.TypesOfChanllenge;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Challenge.
 */
@Entity
@Table(name = "challenge")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Challenge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "intro_text", nullable = false)
    private String introText;

    @Enumerated(EnumType.STRING)
    @Column(name = "challenge_name")
    private TypesOfChanllenge challengeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private Difficulty difficulty;

    @ManyToOne
    @JsonIgnoreProperties(value = { "location", "doors", "challenges", "name" }, allowSetters = true)
    private Room name;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Challenge id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIntroText() {
        return this.introText;
    }

    public Challenge introText(String introText) {
        this.setIntroText(introText);
        return this;
    }

    public void setIntroText(String introText) {
        this.introText = introText;
    }

    public TypesOfChanllenge getChallengeName() {
        return this.challengeName;
    }

    public Challenge challengeName(TypesOfChanllenge challengeName) {
        this.setChallengeName(challengeName);
        return this;
    }

    public void setChallengeName(TypesOfChanllenge challengeName) {
        this.challengeName = challengeName;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public Challenge difficulty(Difficulty difficulty) {
        this.setDifficulty(difficulty);
        return this;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Room getName() {
        return this.name;
    }

    public void setName(Room room) {
        this.name = room;
    }

    public Challenge name(Room room) {
        this.setName(room);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Challenge)) {
            return false;
        }
        return id != null && id.equals(((Challenge) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Challenge{" +
            "id=" + getId() +
            ", introText='" + getIntroText() + "'" +
            ", challengeName='" + getChallengeName() + "'" +
            ", difficulty='" + getDifficulty() + "'" +
            "}";
    }
}
