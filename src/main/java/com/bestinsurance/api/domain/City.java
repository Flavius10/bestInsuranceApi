package com.bestinsurance.api.domain;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "cities")
public class City {

    /// Fields
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    private UUID city_id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column
    private Integer population;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    private State state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @OneToMany(mappedBy = "city")
    private Set<Address> cityAddresses;

    /// Override
    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof City city)) {
            return false;
        }

        return city_id.equals(city.city_id);

    }

    @Override
    public int hashCode(){
        return Objects.hash(city_id);
    }

    @Override
    public String toString(){
        return "City{" +
                "city_id=" + city_id +
                ", name='" + name + '\'' +
                ", population=" + population +
                '}';
    }

    /// Getters and Setters
    public UUID getCity_id() {
        return this.city_id;
    }

    public void setCity_id(final UUID city_id){
        this.city_id = city_id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(final String name){
        this.name = name;
    }

    public Integer getPopulation(){
        return this.population;
    }

    public void setPopulation(final Integer population){
        this.population = population;
    }

    public State getState(){
        return this.state;
    }

    public void setState(final State state){
        this.state = state;
    }

    public Country getCountry(){
        return this.country;
    }

    public void setCountry(final Country country){
        this.country = country;
    }

    public Set<Address> getCityAddresses(){
        return this.cityAddresses;
    }

    public void setCityAddresses(final Set<Address> cityAddresses){
        this.cityAddresses = cityAddresses;
    }

}
