package com.bestinsurance.api.services;

import com.bestinsurance.api.config.DomainConfig;
import com.bestinsurance.api.domain.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DomainConfig.class, CustomerServiceTest.TestConf.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Autowired
    private CustomerService customerService;

    @Test
    public void testUpdate(){

        Customer new_customer = new Customer();
        new_customer.setName("Dummy");
        new_customer.setSurname("Joe");
        new_customer.setEmail("");
        new_customer.setTelephone_number("");
        new_customer.setAddress(null);
        new_customer.setCustomerSubscriptions(null);

        this.customerService.create(new_customer);
        List<Customer> all = customerService.findAll();

        Customer customer = all.get(0);
        logger.info("Found customer: {}", customer.toString());
        String oldEmail = customer.getEmail();
        Customer customerUpdatedFields = new Customer();
        customerUpdatedFields.setEmail("newEmail@email.com");
        Customer updated = customerService.update(customer.getCustomer_id(), customerUpdatedFields);
        logger.info("Updated customer: {}", updated.toString());
        assertEquals(customer.getName(), updated.getName());
        assertEquals(customer.getSurname(), updated.getSurname());
        assertNotEquals(oldEmail, updated.getEmail());
    }

    static class TestConf {

        @Bean
        public CustomerService policyService(){
            return new CustomerService();
        }
    }
}
