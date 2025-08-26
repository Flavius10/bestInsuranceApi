package com.bestinsurance.api.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "policies")
public class Policy implements DomainObject<UUID>{

    /// Fields

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    private UUID policy_id;

    @Column(nullable = false, length = 16)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime created;

    @LastModifiedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime updated;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "policies_coverages",
            joinColumns = @JoinColumn(name = "policy_id"),
            inverseJoinColumns = @JoinColumn(name = "coverage_id")
    )
    private Set<Coverage> coverages;

    @OneToMany(mappedBy = "policy")
    private Set<Subscription> policySubscriptions;

    /// Override

    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof Policy policy)) {
            return false;
        }

        return policy_id.equals(policy.policy_id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(policy_id);
    }

    @Override
    public String toString(){
        return "Policy{" +
                "policy_id=" + policy_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }

    ///  Getters and Setters

    public UUID getPolicy_id() {
        return this.policy_id;
    }

    public void setPolicy_id(final UUID policy_id){
        this.policy_id = policy_id;
    }

    @Override
    public void setId(final UUID id){
        this.setPolicy_id(id);
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

    public BigDecimal getPrice(){
        return this.price;
    }

    public void setPrice(final BigDecimal price){
        this.price = price;
    }

    public OffsetDateTime getCreated(){
        return this.created;
    }

    public void setCreated(final OffsetDateTime created){
        this.created = created;
    }

    public OffsetDateTime getUpdated(){
        return this.updated;
    }

    public void setUpdated(final OffsetDateTime updated){
        this.updated = updated;
    }

    public Set<Coverage> getPoliciesCoverages(){
        return this.coverages;
    }

    public void setPoliciesCoverages(final Set<Coverage> policiesCoverages){
        this.coverages = policiesCoverages;
    }

    public Set<Subscription> getPolicySubscriptions(){
        return this.policySubscriptions;
    }

    public void setPolicySubscriptions(final Set<Subscription> policySubscriptions){
        this.policySubscriptions = policySubscriptions;
    }

}
