package com.bestinsurance.api.controllers;

import com.bestinsurance.api.domain.*;
import com.bestinsurance.api.dto.customer.CustomerCreation;
import com.bestinsurance.api.dto.customer.CustomerUpdate;
import com.bestinsurance.api.dto.customer.CustomerView;
import com.bestinsurance.api.jpa.AbstractCustomerInitializedTest;
import com.bestinsurance.api.repos.*;
import com.bestinsurance.api.rest.CustomerController;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Propagation;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerControllerTest extends AbstractCustomerInitializedTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public void initOM(){
        om.registerModule(new JavaTimeModule());
    }

    @Test
    public void testAllFiltersInitializedOrderByNameASC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.NAME).setOrderDirection("ASC").runTest(3);
    }

    @Test
    public void testAllFiltersInitializedOrderByNameDESC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.NAME).setOrderDirection("DESC").runTest(3);
    }

    @Test
    public void testAllFiltersInitializedOrderBySurnameASC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.SURNAME).setOrderDirection("ASC").runTest(3);
    }

    @Test
    public void testAllFiltersInitializedOrderBySurnameDESC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.SURNAME).setOrderDirection("DESC").runTest(3);
    }

    @Test
    public void testAllFiltersInitializedOrderByEmailASC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.EMAIL).setOrderDirection("ASC").runTest(3);
    }

    @Test
    public void testAllFiltersInitializedOrderByEmailDESC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.EMAIL).setOrderDirection("DESC").runTest(3);
    }

    @Test
    public void testAllFiltersInitializedOrderByAgeASC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.AGE).setOrderDirection("ASC").runTest(3);
    }

    @Test
    public void testAllFiltersInitializedOrderByAgeDESC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.AGE).setOrderDirection("DESC").runTest(3);
    }

    @Test
    public void testAllFiltersInitialized() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").runTest(3);
    }

    public class CustomerSearchTestHelper{
        private String name;
        private String surname;
        private String email;
        private String age;
        private String ageFrom;
        private String ageTo;
        private String orderBy;
        private String orderDirection;

        private void runTest(int expectedResults) throws Exception{
            MvcResult mvcResult = mockMvc.perform(get("/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam(CustomerController.NAME , name)
                    .queryParam(CustomerController.SURNAME, surname)
                    .queryParam(CustomerController.EMAIL, email)
                    .queryParam(CustomerController.AGE_FROM, ageFrom)
                    .queryParam(CustomerController.AGE_TO, ageTo)
                    .queryParam(CustomerController.ORDERBY, orderBy)
                    .queryParam(CustomerController.ORDERDIRECTION, orderDirection))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(expectedResults)))
                .andReturn();

            boolean ascending = orderDirection == null || !orderDirection.equals("DESC");

            Comparator<CustomerView> comparator;

            if (orderBy == null){
                comparator = customerComparedByName();
            } else{
                switch(orderBy.toLowerCase()){
                    case CustomerController.NAME: comparator = customerComparedByName();
                    case CustomerController.SURNAME: comparator = customerComparedBySurname();
                    case CustomerController.EMAIL: comparator = customerComparedByEmail();
                    case CustomerController.AGE: comparator = customerComparedByAge();

                    default: comparator = customerComparedByName();
                }
            }

            assertTrue(compareCustomerOrder(om.readValue(mvcResult.getResponse().getContentAsString(), CustomerView[].class), comparator, ascending));
        }

        private boolean compareCustomerOrder(CustomerView[] customers, Comparator<CustomerView> comparator, boolean ascendingOrder){
            for (int i = 1; i < customers.length; i++) {
               if (ascendingOrder) {
                   if (comparator.compare(customers[i], customers[i - 1]) < 0) return false;
               } else {
                   if (comparator.compare(customers[i], customers[i - 1]) > 0) return false;
               }
            }
            return true;
        }

        private Comparator<CustomerView> customerComparedByName(){
            return (c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName());
        }

        private Comparator<CustomerView> customerComparedBySurname(){
            return (c1, c2) -> c1.getSurname().compareToIgnoreCase(c2.getSurname());
        }

        private Comparator<CustomerView> customerComparedByEmail(){
            return (c1, c2) -> c1.getEmail().compareToIgnoreCase(c2.getEmail());
        }

        private Comparator<CustomerView> customerComparedByAge(){
            return Comparator.comparing(CustomerView::getBirthDate);
        }

        public CustomerSearchTestHelper setName(String name) {
            this.name = name;
            return this;
        }

        public CustomerSearchTestHelper setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public CustomerSearchTestHelper setEmail(String email) {
            this.email = email;
            return this;
        }

        public CustomerSearchTestHelper setAgeFrom(String ageFrom) {
            this.ageFrom = ageFrom;
            return this;
        }

        public CustomerSearchTestHelper setAgeTo(String ageTo) {
            this.ageTo = ageTo;
            return this;
        }

        public CustomerSearchTestHelper setOrderBy(String orderBy) {
            this.orderBy = orderBy;
            return this;
        }

        public CustomerSearchTestHelper setOrderDirection(String orderDirection) {
            this.orderDirection = orderDirection;
            return this;
        }
    }

}
