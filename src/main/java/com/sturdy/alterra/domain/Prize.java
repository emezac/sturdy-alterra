package com.sturdy.alterra.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Prize.
 */
@Entity
@Table(name = "prize")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Prize implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "prize_name", nullable = false)
    private String prizeName;

    @Column(name = "pips")
    private Long pips;

    @Column(name = "expire_date")
    private Instant expireDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "prizes", "name" }, allowSetters = true)
    private Door name;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Prize id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPrizeName() {
        return this.prizeName;
    }

    public Prize prizeName(String prizeName) {
        this.setPrizeName(prizeName);
        return this;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public Long getPips() {
        return this.pips;
    }

    public Prize pips(Long pips) {
        this.setPips(pips);
        return this;
    }

    public void setPips(Long pips) {
        this.pips = pips;
    }

    public Instant getExpireDate() {
        return this.expireDate;
    }

    public Prize expireDate(Instant expireDate) {
        this.setExpireDate(expireDate);
        return this;
    }

    public void setExpireDate(Instant expireDate) {
        this.expireDate = expireDate;
    }

    public Door getName() {
        return this.name;
    }

    public void setName(Door door) {
        this.name = door;
    }

    public Prize name(Door door) {
        this.setName(door);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prize)) {
            return false;
        }
        return id != null && id.equals(((Prize) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prize{" +
            "id=" + getId() +
            ", prizeName='" + getPrizeName() + "'" +
            ", pips=" + getPips() +
            ", expireDate='" + getExpireDate() + "'" +
            "}";
    }
}
