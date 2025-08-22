package com.bestinsurance.api.dto;

public class AddressView {

    private String id;
    private String address;
    private String postal_code;
    private CityView city;
    private CountryView country;
    private StateView state;

    public String getId(){
        return this.id;
    }

    public void setId(final String id){
        this.id = id;
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

    public CityView getCity(){
        return this.city;
    }

    public void setCity(final CityView city){
        this.city = city;
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

}
