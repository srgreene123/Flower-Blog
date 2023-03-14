package org.jhipster.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Flower.
 */
@Entity
@Table(name = "flower")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Flower implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "season")
    private String season;

    @Column(name = "description")
    private String description;

    @Column(name = "image_link")
    private String imageLink;

    @JsonIgnoreProperties(value = { "user", "flower" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Post post;

    @ManyToMany
    @JoinTable(
        name = "rel_flower__location",
        joinColumns = @JoinColumn(name = "flower_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "flowers" }, allowSetters = true)
    private Set<Location> locations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Flower id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Flower name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeason() {
        return this.season;
    }

    public Flower season(String season) {
        this.setSeason(season);
        return this;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getDescription() {
        return this.description;
    }

    public Flower description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLink() {
        return this.imageLink;
    }

    public Flower imageLink(String imageLink) {
        this.setImageLink(imageLink);
        return this;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Flower post(Post post) {
        this.setPost(post);
        return this;
    }

    public Set<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Flower locations(Set<Location> locations) {
        this.setLocations(locations);
        return this;
    }

    public Flower addLocation(Location location) {
        this.locations.add(location);
        location.getFlowers().add(this);
        return this;
    }

    public Flower removeLocation(Location location) {
        this.locations.remove(location);
        location.getFlowers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Flower)) {
            return false;
        }
        return id != null && id.equals(((Flower) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Flower{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", season='" + getSeason() + "'" +
            ", description='" + getDescription() + "'" +
            ", imageLink='" + getImageLink() + "'" +
            "}";
    }
}
