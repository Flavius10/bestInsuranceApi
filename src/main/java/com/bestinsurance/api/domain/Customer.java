package com.bestinsurance.api.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "customers")
public class Customer implements DomainObject<UUID>{

    /// Fields
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    private UUID customer_id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(nullable = false, length = 64)
    private String surname;

    @Column(nullable = false, length = 320)
    private String email;

    @Column(length = 20)
    private String telephone_number;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime created;

    @LastModifiedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime updated;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", insertable = true)
    private Address address;

    @OneToMany(mappedBy = "customer")
    private Set<Subscription> customerSubscriptions;

    /// Override

    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof Customer customer)) {
            return false;
        }

        return customer_id.equals(customer.customer_id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(customer_id);
    }

    @Override
    public String toString(){
        return "Customer{" +
                "customer_id=" + customer_id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", address=" + address +
                '}';
    }

    /// Getters and Setters
    public UUID getCustomer_id() {
        return this.customer_id;
    }

    public void setCustomer_id(final UUID customer_id){
        this.customer_id = customer_id;
    }

    @Override
    public void setId(final UUID id){
        this.setCustomer_id(id);
    }

    public String getName(){
        return this.name;
    }

    public void setName(final String name){
        this.name = name;
    }

    public String getSurname(){
        return this.surname;
    }

    public void setSurname(final String surname){
        this.surname = surname;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(final String email){
        this.email = email;
    }

    public String getTelephone_number(){
        return this.telephone_number;
    }

    public void setTelephone_number(final String telephone_number){
        this.telephone_number = telephone_number;
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

    public Address getAddress(){
        return this.address;
    }

    public void setAddress(final Address address){
        this.address = address;
    }

    public Set<Subscription> getCustomerSubscriptions(){
        return this.customerSubscriptions;
    }

    public void setCustomerSubscriptions(final Set<Subscription> customerSubscriptions){
        this.customerSubscriptions = customerSubscriptions;
    }
}
