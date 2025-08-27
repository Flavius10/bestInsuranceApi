package com.bestinsurance.api.controllers;

import com.bestinsurance.api.domain.*;
import com.bestinsurance.api.dto.customer.CustomerCreation;
import com.bestinsurance.api.dto.customer.CustomerUpdate;
import com.bestinsurance.api.dto.customer.CustomerView;
import com.bestinsurance.api.repos.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    @Commit
    @Transactional
    public void testCreateCustomers() throws Exception{

        CustomerCreation customerCreation = new CustomerCreation();

        om.registerModule(new JavaTimeModule());

        customerCreation.setName("Dummy");
        customerCreation.setSurname("Joe");
        customerCreation.setEmail("testEmail@gmail.com");
        customerCreation.setAddress("Dummy Street 123");
        customerCreation.setPostal_code("12345");
        customerCreation.setBirthDate(LocalDate.of(1990, 5, 15));
        customerCreation.setIdCountry("11111111-1111-1111-1111-111111111111");
        customerCreation.setIdState("aaaaaaa3-0000-0000-0000-aaaaaaaaaaa3");
        customerCreation.setIdCity("45576d7c-8d84-4422-9440-19ef80fa16f3");

        MvcResult mvcResult = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(customerCreation)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        Optional<Customer> customer = customerRepository.findById(UUID.fromString(id));
        assertTrue(customer.isPresent());
        assertNotNull(customer.get().getAddress());
        assertNotNull(customer.get().getAddress().getAddress_id());
        assertNotNull(customer.get().getAddress().getCountry());
        assertNotNull(customer.get().getAddress().getCountry().getCountry_id());
        assertNotNull(customer.get().getAddress().getState().getState_id());
        assertNotNull(customer.get().getAddress().getCity().getCity_id());
    }

    @Test
    @Transactional
    @Commit
    public void testAll() throws Exception {

        CustomerView customerCreation = new CustomerView();

        Country country = new Country();
        country.setName("USA");
        countryRepository.save(country);


        State state = new State();
        state.setName("California");
        state.setCountry(country);
        stateRepository.save(state);


        City city = new City();
        city.setName("Los Angeles");
        city.setState(state);
        city.setCountry(country);
        cityRepository.save(city);


        Address address = new Address();
        address.setAddress("123 Dummy Street");
        address.setPostal_code("12345");
        address.setCountry(country);
        address.setState(state);
        address.setCity(city);


        Customer customer = new Customer();
        customer.setName("John");
        customer.setSurname("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setTelephone_number("123-456-7890");
        customer.setBirthDate(LocalDate.of(1990, 5, 15));
        customer.setAddress(address);

        customerRepository.save(customer);


        MvcResult mvcResult = mockMvc.perform(get("/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //if (mvcResult.getResponse().getContentAsString().equals("[]")) return;
        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$[0].id");
        List<Customer> listCustomers = new ArrayList<>();
        customerRepository.findAll().iterator().forEachRemaining(listCustomers::add);
        assertFalse(listCustomers.isEmpty());
        assertTrue(listCustomers.stream().anyMatch(customerAssert -> customerAssert.getCustomer_id().toString().equals(id)));
    }

    @Test
    @Transactional
    @Commit
    public void testGetById() throws Exception {
        Country country = new Country();
        country.setName("USA");
        countryRepository.save(country);

        State state = new State();
        state.setName("California");
        state.setCountry(country);
        stateRepository.save(state);

        City city = new City();
        city.setName("Los Angeles");
        city.setState(state);
        city.setCountry(country);
        cityRepository.save(city);

        Address address = new Address();
        address.setAddress("123 Dummy Street");
        address.setPostal_code("12345");
        address.setCountry(country);
        address.setState(state);
        address.setCity(city);


        Customer customer = new Customer();
        customer.setName("John");
        customer.setSurname("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setTelephone_number("123-456-7890");
        customer.setBirthDate(LocalDate.of(1990, 5, 15));
        customer.setAddress(address);
        customerRepository.save(customer);


        UUID customerId = customer.getCustomer_id();

        MvcResult mvcResult = mockMvc.perform(get("/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId.toString()))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andReturn();

        Optional<Customer> customerFromDb = customerRepository.findById(customerId);
        assertTrue(customerFromDb.isPresent());
        assertEquals("John", customerFromDb.get().getName());
        assertEquals("Doe", customerFromDb.get().getSurname());
    }


    @Test
    @Transactional
    @Commit
    public void testUpdate() throws Exception {
        // Setup initial customer
        Country country = new Country();
        country.setName("USA");
        countryRepository.save(country);

        State state = new State();
        state.setName("California");
        state.setCountry(country);
        stateRepository.save(state);

        City city = new City();
        city.setName("Los Angeles");
        city.setState(state);
        city.setCountry(country);
        cityRepository.save(city);

        Address address = new Address();
        address.setAddress("123 Dummy Street");
        address.setPostal_code("12345");
        address.setCountry(country);
        address.setState(state);
        address.setCity(city);

        Customer customer = new Customer();
        customer.setName("John");
        customer.setSurname("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setTelephone_number("123-456-7890");
        customer.setBirthDate(LocalDate.of(1990, 5, 15));
        customer.setAddress(address);
        customerRepository.save(customer);

        UUID customerId = customer.getCustomer_id();

        CustomerUpdate customerUpdate = new CustomerUpdate();
        customerUpdate.setEmail("flavius_test_email@gmail.com");
        customerUpdate.setIdCountry(country.getCountry_id().toString());
        customerUpdate.setIdState(state.getState_id().toString());
        customerUpdate.setIdCity(city.getCity_id().toString());

        mockMvc.perform(put("/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(customerUpdate)))
                .andDo(print())
                .andReturn();

        Optional<Customer> customerFromDb = customerRepository.findById(customerId);
        assertTrue(customerFromDb.isPresent());
        assertEquals("John", customerFromDb.get().getName());
        assertEquals("Doe", customerFromDb.get().getSurname());
        assertEquals("john.doe@example.com", customerFromDb.get().getEmail());
    }


    @Test
    @Transactional
    @Commit
    public void testDelete() throws Exception {
        Country country = new Country();
        country.setName("USA");
        countryRepository.save(country);

        State state = new State();
        state.setName("California");
        state.setCountry(country);
        stateRepository.save(state);

        City city = new City();
        city.setName("Los Angeles");
        city.setState(state);
        city.setCountry(country);
        cityRepository.save(city);

        Address address = new Address();
        address.setAddress("123 Dummy Street");
        address.setPostal_code("12345");
        address.setCountry(country);
        address.setState(state);
        address.setCity(city);


        Customer customer = new Customer();
        customer.setName("John");
        customer.setSurname("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setTelephone_number("123-456-7890");
        customer.setBirthDate(LocalDate.of(1990, 5, 15));
        customer.setAddress(address);
        customerRepository.save(customer);


        UUID customerId = customer.getCustomer_id();

        MvcResult mvcResult = mockMvc.perform(delete("/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Optional<Customer> customerFromDb = customerRepository.findById(customerId);
        assertFalse(customerFromDb.isPresent());
    }


}
