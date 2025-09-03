package com.bestinsurance.api.controllers;

import com.bestinsurance.api.jpa.AbstractCustomerInitializedTest;
import com.bestinsurance.api.jpa.AbstractCustomerWithAssociationsTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class CustomerControllerSearchTest extends AbstractCustomerWithAssociationsTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public void initOM(){
        om.registerModule(new JavaTimeModule());
    }

    @Test
    public void testSearchByPolicy() throws Exception{
        mockMvc.perform(get("/customers/policy/{id}", policies.get(0).getPolicy_id().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testSearchByCoverage() throws Exception{
        mockMvc.perform(get("/customers/coverage/{id}", coverages.get(0).getCoverage_id().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testSearchByDiscount() throws Exception{
        mockMvc.perform(get("/customers/subscriptions/discountedPrice")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
    }

    @Test
    public void testSearchByDate() throws Exception{
        LocalDate startDate = LocalDate.now().minusDays(1);
        String startDateString = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.now().plusMonths(7);
        String endDateString = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        mockMvc.perform(get("/customers/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                .queryParam("startDate", startDateString)
                .queryParam("endDate", endDateString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
    }

}
