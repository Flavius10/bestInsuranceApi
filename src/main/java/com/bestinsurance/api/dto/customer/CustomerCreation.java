package com.bestinsurance.api.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CustomerCreation {

    @NotBlank
    @Size(max = 64)
    private String name;

    @NotBlank
    @Size(max = 64)
    private String surname;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private LocalDate birthDate;

    @NotBlank
    @Size(max = 128)
    private String address;

    @NotBlank
    private String postal_code;

    @NotBlank
    private String idCountry;

    @NotBlank
    private String idCity;

    @NotBlank
    private String idState;

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

    public LocalDate getBirthDate(){
        return this.birthDate;
    }

    public void setBirthDate(final LocalDate birthDate){
        this.birthDate = birthDate;
    }

    public String getAddress(){
        return this.address;
    }

    public void setAddress(final String address){
        this.address = address;
    }

    public String getPostal_code(){
        return this.postal_code;
    }

    public void setPostal_code(final String postal_code){
        this.postal_code = postal_code;
    }

    public String getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(final String idCountry) {
        this.idCountry = idCountry;
    }

    public String getIdCity() {
        return idCity;
    }

    public void setIdCity(final String idCity) {
        this.idCity = idCity;
    }

    public String getIdState() {
        return idState;
    }

    public void setIdState(final String idState) {
        this.idState = idState;
    }
}