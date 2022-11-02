package com.sturdy.alterra.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Profile.
 */
@Entity
@Table(name = "profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "auth_0_user_id", nullable = false)
    private String auth0UserId;

    @Column(name = "social_network")
    private String socialNetwork;

    @Column(name = "acl_setup")
    private String aclSetup;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Profile id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Profile name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuth0UserId() {
        return this.auth0UserId;
    }

    public Profile auth0UserId(String auth0UserId) {
        this.setAuth0UserId(auth0UserId);
        return this;
    }

    public void setAuth0UserId(String auth0UserId) {
        this.auth0UserId = auth0UserId;
    }

    public String getSocialNetwork() {
        return this.socialNetwork;
    }

    public Profile socialNetwork(String socialNetwork) {
        this.setSocialNetwork(socialNetwork);
        return this;
    }

    public void setSocialNetwork(String socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public String getAclSetup() {
        return this.aclSetup;
    }

    public Profile aclSetup(String aclSetup) {
        this.setAclSetup(aclSetup);
        return this;
    }

    public void setAclSetup(String aclSetup) {
        this.aclSetup = aclSetup;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profile)) {
            return false;
        }
        return id != null && id.equals(((Profile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Profile{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", auth0UserId='" + getAuth0UserId() + "'" +
            ", socialNetwork='" + getSocialNetwork() + "'" +
            ", aclSetup='" + getAclSetup() + "'" +
            "}";
    }
}
