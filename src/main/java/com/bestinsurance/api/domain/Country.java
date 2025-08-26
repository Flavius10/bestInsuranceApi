package com.bestinsurance.api.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.DialectOverride;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "countries")
public class Country implements DomainObject<UUID>{

    /// Fields
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    private UUID country_id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column
    private Integer population;

    @OneToMany(mappedBy = "country")
    private Set<State> countryStates;

    @OneToMany(mappedBy = "country")
    private Set<City> countryCities;

    @OneToMany(mappedBy = "country")
    private Set<Address> countryAddresses;


    /// Overrides
    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof Country country)) {
            return false;
        }

        return country_id.equals(country.country_id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(country_id);
    }

    @Override
    public String toString(){
        return "Country{" +
                "country_id=" + country_id +
                ", name='" + name + '\'' +
                ", population=" + population +
                '}';
    }

    ///Getters and Setters
    public UUID getCountry_id() {
        return this.country_id;
    }

    public void setCountry_id(final UUID country_id) {
        this.country_id = country_id;
    }

    @Override
    public void setId(final UUID id){
        this.setCountry_id(id);
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getPopulation(){
        return this.population;
    }

    public void setPopulation(final Integer population) {
        this.population = population;
    }

    public Set<State> getCountryStates() {
        return this.countryStates;
    }

    public void setCountryStates(final Set<State> countryStates) {
        this.countryStates = countryStates;
    }

    public Set<City> getCountryCities() {
        return this.countryCities;
    }

    public void setCountryCities(final Set<City> countryCities) {
        this.countryCities = countryCities;
    }

    public Set<Address> getCountryAddresses() {
        return this.countryAddresses;
    }

    public void setCountryAddresses(final Set<Address> countryAddresses) {
        this.countryAddresses = countryAddresses;
    }

}