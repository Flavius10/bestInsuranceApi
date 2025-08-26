package com.bestinsurance.api.dto.subscription;

import com.bestinsurance.api.dto.customer.CustomerView;
import com.bestinsurance.api.dto.policy.PolicyView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class SubscriptionView {

    private PolicyView policyId;
    private CustomerView customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal price;
    private OffsetDateTime created;
    private OffsetDateTime updated;

    public PolicyView getPolicyId() {
        return policyId;
    }

    public void setPolicyId(final PolicyView policyId) {
        this.policyId = policyId;
    }

    public CustomerView getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final CustomerView customerId) {
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

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(final OffsetDateTime created) {
        this.created = created;
    }

    public OffsetDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(final OffsetDateTime updated) {
        this.updated = updated;
    }
}
