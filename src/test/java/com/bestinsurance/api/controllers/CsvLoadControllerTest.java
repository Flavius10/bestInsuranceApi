package com.bestinsurance.api.controllers;

import com.bestinsurance.api.domain.Customer;
import com.bestinsurance.api.jpa.AbstractPolicyInitializedTest;
import com.bestinsurance.api.repos.CustomerRepository;
import com.bestinsurance.api.repos.PolicyRepository;
import com.bestinsurance.api.services.CsvService;
import com.bestinsurance.api.services.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.junit.MockitoJUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.AssertJUnit.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CsvLoadControllerTest extends AbstractPolicyInitializedTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private CsvService csvService;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeAll
    public void initOM(){
        om.registerModule(new JavaTimeModule());
    }

    @Test
    public void testCsvLoadSubscriptions() throws Exception {
        ClassPathResource res = new ClassPathResource("customers_dummy.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(res.getFile()));
        mockMvc.perform(multipart("/subscriptions/upload")
                        .file(multipartFile));
        Iterable<Customer> all = customerRepository.findAll();
        List<Customer> customers = new ArrayList<>();
        all.forEach(customers::add);
        assertEquals(0, customers.size());
    }

}
