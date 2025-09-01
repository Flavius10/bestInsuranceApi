package com.bestinsurance.api.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CsvSubscriptions {

    @CsvBindByName
    private String name;

    @CsvBindByName
    private String surname;

    @CsvBindByName
    private String email;

    @CsvBindByName
    @CsvDate("yyyy-MM-dd")
    private LocalDate birthDate;

    @CsvBindByName
    private String address;

    @CsvBindByName
    private String postal_code;

    @CsvBindByName
    private String stateName;

    @CsvBindByName
    private String cityName;

    @CsvBindByName
    private String policyName;

    @CsvBindByName
    private BigDecimal paidPrice;

    @CsvBindByName
    @CsvDate("yyyy-MM-dd")
    private LocalDate startDate;

    @CsvBindByName
    @CsvDate("yyyy-MM-dd")
    private LocalDate endDate;

    public CsvSubscriptions() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public BigDecimal getPrice() {
        return paidPrice;
    }

    public void setPrice(BigDecimal price) {
        this.paidPrice = price;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}