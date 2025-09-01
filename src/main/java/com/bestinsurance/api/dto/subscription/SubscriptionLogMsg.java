package com.bestinsurance.api.dto.subscription;

import java.time.LocalDate;
import java.util.UUID;

public class SubscriptionLogMsg {

    private UUID policyId;
    private UUID customerId;
    private String customerName;
    private String customerSurname;
    private String customerEmail;
    private String customerTelephone;
    private String policyName;
    private LocalDate subscriptionStart;
    private LocalDate subscriptionEnd;

    public SubscriptionLogMsg() {
    }

    public UUID getPolicyId() {
        return policyId;
    }
    public void setPolicyId(UUID policyId) {
        this.policyId = policyId;
    }
    public UUID getCustomerId() {
        return customerId;
    }
    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getCustomerSurname() {
        return customerSurname;
    }
    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    public String getCustomerTelephone() {
        return customerTelephone;
    }
    public void setCustomerTelephone(String customerTelephone) {
        this.customerTelephone = customerTelephone;
    }

    public String getPolicyName() {
        return policyName;
    }
    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }
    public LocalDate getSubscriptionStart() {
        return subscriptionStart;
    }
    public void setSubscriptionStart(LocalDate subscriptionStart) {
        this.subscriptionStart = subscriptionStart;
    }
    public LocalDate getSubscriptionEnd() {
        return subscriptionEnd;
    }
    public void setSubscriptionEnd(LocalDate subscriptionEnd) {
        this.subscriptionEnd = subscriptionEnd;
    }
}
