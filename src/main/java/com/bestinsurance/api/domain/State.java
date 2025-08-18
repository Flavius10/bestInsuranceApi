package com.bestinsurance.api.domain;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "states")
public class State {

    /// Fields
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    private UUID state_id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column
    private Integer population;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @OneToMany(mappedBy = "state")
    private Set<City> stateCities;

    @OneToMany(mappedBy = "state")
    private Set<Address> stateAddresses;


    /// Override
    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof State state)) {
            return false;
        }

        return state_id.equals(state.state_id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(state_id);
    }

    @Override
    public String toString(){
        return "State{" +
                "state_id=" + state_id +
                ", name='" + name + '\'' +
                ", population=" + population +
                '}';
    }

    /// Getters and Setters
    public UUID getState_id(){
        return this.state_id;
    }

    public void setState_id(final UUID state_id){
        this.state_id = state_id;
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

    public Country getCountry(){
        return this.country;
    }

    public void setCountry(final Country country){
        this.country = country;
    }

    public Set<City> getStateCities(){
        return this.stateCities;
    }

    public void setStateCities(final Set<City> stateCities){
        this.stateCities = stateCities;
    }

    public Set<Address> getStateAddresses(){
        return this.stateAddresses;
    }

    public void setStateAddresses(final Set<Address> stateAddresses){
        this.stateAddresses = stateAddresses;
    }

}
