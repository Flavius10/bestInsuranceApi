package com.bestinsurance.api.domain;

import jakarta.persistence.*;
import org.w3c.dom.Text;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "coverages")
public class Coverage {


    /// Fields
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    private UUID coverage_id;

    @Column(nullable = false, length = 16)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "coverages")
    private Set<Policy> policies;

    /// Override
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Coverage coverage)) {
            return false;
        }

        return coverage_id.equals(coverage.coverage_id);

    }

    @Override
    public int hashCode(){
        return Objects.hash(coverage_id);
    }

    @Override
    public String toString(){
        return "Coverage{" +
                "coverage_id=" + coverage_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    /// Getters and Setters
    public UUID getCoverage_id() {
        return this.coverage_id;
    }

    public void setCoverage_id(final UUID coverage_id) {
        this.coverage_id = coverage_id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(final String name){
        this.name = name;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(final String description){
        this.description = description;
    }

    public Set<Policy> getPoliciesCoverages(){
        return this.policies;
    }

    public void setPoliciesCoverages(final Set<Policy> policiesCoverages){
        this.policies = policiesCoverages;
    }

}
