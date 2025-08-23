package com.bestinsurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerView {

    @NotBlank
    private String id;

    @NotBlank
    @Size(max = 64)
    private String name;

    @NotBlank
    @Size(max = 64)
    private String surname;

    @NotBlank
    @Email
    private String email;

    private AddressView address;
    private CountryView country;
    private StateView state;
    private CityView city;

    public String getId(){
        return this.id;
    }

    public void setId(final String id){
        this.id = id;
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

    public AddressView getAddress(){
        return this.address;
    }

    public void setAddress(final AddressView address){
        this.address = address;
    }

    public CountryView getCountry(){
        return this.country;
    }

    public void setCountry(final CountryView country){
        this.country = country;
    }

    public StateView getState(){
        return this.state;
    }

    public void setState(final StateView state){
        this.state = state;
    }

    public CityView getCity(){
        return this.city;
    }

    public void setCity(final CityView city){
        this.city = city;
    }
}
