package com.bestinsurance.api.dto.subscription;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SubscriptionCreation {

    @NotBlank
    private String policyId;

    @NotBlank
    private String customerId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    private BigDecimal price;

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(final String policyId) {
        this.policyId = policyId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final String customerId) {
        this.customerId = customerId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

}
