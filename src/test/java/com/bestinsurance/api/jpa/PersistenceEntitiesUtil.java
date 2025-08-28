package com.bestinsurance.api.jpa;

import com.bestinsurance.api.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PersistenceEntitiesUtil {

    public static String[] cityIds = {"45576d7c-8d84-4422-9440-19ef80fa16f3",
            "91f360d5-811b-417c-a202-f5ba4b34b895",
            "144b05b6-ebf6-43a8-836d-0998c2c20a3c",
            "74716a04-d538-4441-84bf-7c41470778ca",
            "eb5e9505-8580-4857-9195-6bee0324ac0f"};

    public static Customer instanceCustomer(String name, String surname, String email, LocalDate birthDate, Address address) {
        Customer c = new Customer();
        c.setName(name);
        c.setSurname(surname);
        c.setEmail(email);
        c.setBirthDate(birthDate);
        c.setAddress(address);
        return c;
    }

    public static Address instanceAddress(String street, String postalCode, City city) {
        Address a = new Address();
        a.setCity(city);
        State s = new State();
        s.setState_id(city.getState().getState_id());
        a.setState(s);
        Country co = new Country();
        co.setCountry_id(city.getCountry().getCountry_id());
        a.setCountry(co);
        a.setAddress(street);
        a.setPostal_code(postalCode);
        return a;
    }

    public static Policy instancePolicy(String name, String description, BigDecimal price) {
        Policy policy = new Policy();
        policy.setName(name);
        policy.setDescription(description);
        policy.setPrice(price);
        return policy;
    }

    public static Policy instancePolicy(String name, String description, BigDecimal price, Coverage... coverages) {
        Policy policy = new Policy();
        policy.setName(name);
        policy.setDescription(description);
        policy.setPrice(price);
        policy.setPoliciesCoverages(Arrays.stream(coverages).collect(Collectors.toSet()));
        return policy;
    }

    public static Subscription instanceSubscription(Customer customer, Policy policy, BigDecimal paidPrice,
                                                    LocalDate startDate, LocalDate endDate) {
        Subscription subscription = new Subscription();
        subscription.setCustomer(customer);
        subscription.setPolicy(policy);
        subscription.setPaidPrice(paidPrice);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        SubscriptionId id = new SubscriptionId();
        id.setCustomerId(customer.getCustomer_id());
        id.setPolicyId(policy.getPolicy_id());
        subscription.setId(id);
        return subscription;
    }

    public static Coverage instanceCoverage(String name, String description) {
        Coverage coverage = new Coverage();
        coverage.setName(name);
        coverage.setDescription(description);
        return coverage;
    }

}
