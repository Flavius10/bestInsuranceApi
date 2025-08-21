package com.bestinsurance.api.jpa;

import com.bestinsurance.api.config.DomainConfig;
import com.bestinsurance.api.domain.Customer;
import com.bestinsurance.api.repos.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(DomainConfig.class)
public class CustomerJPATest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testIdInitialize() {

        Customer customer = new Customer();
        customer.setName("Dummy");
        customer.setEmail("Test_email");
        customer.setSurname("Joe");
        customer.setAddress(new com.bestinsurance.api.domain.Address());
        customer.setCustomerSubscriptions(new java.util.HashSet<>());

        /// saving the customer for the test
        Customer created_customer = customerRepository.save(customer);

        Assertions.assertNotNull(created_customer.getCustomer_id());
        Assertions.assertNotNull(created_customer.getCreated());
        Assertions.assertNotNull(created_customer.getUpdated());

        Customer found_customer = customerRepository.findById(created_customer.getCustomer_id()).get();
        Assertions.assertEquals(found_customer.getCustomer_id(), created_customer.getCustomer_id());
        Assertions.assertEquals(found_customer.getName(), created_customer.getName());
        Assertions.assertEquals(found_customer.getAddress(), created_customer.getAddress());
        Assertions.assertEquals(found_customer.getSurname(), created_customer.getSurname());
        Assertions.assertEquals(found_customer.getCreated(), created_customer.getCreated());
    }
}
