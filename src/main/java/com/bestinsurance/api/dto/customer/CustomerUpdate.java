package com.bestinsurance.api.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerUpdate {

    @NotBlank
    @Email
    private String email;

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

    public String getEmail(){
        return this.email;
    }

    public void setEmail(final String email){
        this.email = email;
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

    public String getIdCountry(){
        return this.idCountry;
    }

    public void setIdCountry(final String idCountry){
        this.idCountry = idCountry;
    }

    public String getIdCity(){
        return this.idCity;
    }

    public void setIdCity(final String idCity){
        this.idCity = idCity;
    }

    public String getIdState(){
        return this.idState;
    }

    public void setIdState(final String idState){
        this.idState = idState;
    }

}
