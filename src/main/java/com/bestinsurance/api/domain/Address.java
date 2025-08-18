package com.bestinsurance.api.domain;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "addresses")
public class Address {

    /// Fields
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    private UUID address_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false, insertable = true)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id", insertable = true)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false, insertable = true)
    private City city;

    @Column(nullable = false, length = 128)
    private String address;

    @Column(length = 16)
    private String postal_code;

    /// Override

    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof Address address)) {
            return false;
        }

        return address_id.equals(address.address_id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(address_id);
    }

    @Override
    public String toString(){
        return "Address{" +
                "address_id=" + address_id +
                ", country=" + country +
                ", state=" + state +
                ", city=" + city +
                ", address='" + address + '\'' +
                ", postal_code='" + postal_code + '\'' +
                '}';
    }

    /// Getters and Setters

    public UUID getAddress_id() {
        return this.address_id;
    }

    public void setAddress_id(final UUID address_id){
        this.address_id = address_id;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(final Country country){
        this.country = country;
    }

    public State getState() {
        return this.state;
    }

    public void setState(final State state){
        this.state = state;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(final City city){
        this.city = city;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(final String address){
        this.address = address;
    }

    public String getPostal_code() {
        return this.postal_code;
    }

    public void setPostal_code(final String postal_code){
        this.postal_code = postal_code;
    }
}
