package com.bestinsurance.api.controllers;

import com.bestinsurance.api.domain.Policy;
import com.bestinsurance.api.dto.policy.PolicyCreation;
import com.bestinsurance.api.dto.policy.PolicyView;
import com.bestinsurance.api.jpa.AbstractPolicyInitializedTest;
import com.bestinsurance.api.repos.PolicyRepository;
import com.bestinsurance.api.rest.PolicyController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockReset;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PolicyControllerTest extends AbstractPolicyInitializedTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public void initOM(){
        om.registerModule(new JavaTimeModule());
    }

    @Test
    public void testPolicyCreation() throws Exception{
        PolicyCreation policyCreation = new PolicyCreation();
        policyCreation.setName("test");
        policyCreation.setDescription("description");
        policyCreation.setPrice(new BigDecimal(100));
        List<String> coveragesIds = Lists.newArrayList();
        coveragesIds.add(coverages.get(0).getCoverage_id().toString());
        coveragesIds.add(coverages.get(1).getCoverage_id().toString());
        coveragesIds.add(coverages.get(2).getCoverage_id().toString());

        policyCreation.setCoveragesIds(coveragesIds);
        MvcResult mvcResult = mockMvc.perform(post("/policies")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                .content(om.writeValueAsString(policyCreation)))
                .andReturn();
    }

    @Test
    public void testPriceNameSearch() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .queryParam(PolicyController.NAME_CONTAINS, "Silver")
                        .queryParam(PolicyController.PRICE , "100")
                        .queryParam(PolicyController.ORDERBY , "PRICE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(20)))
                .andReturn();
        assertTrue("Merge", checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .queryParam(PolicyController.NAME_CONTAINS, "Silver")
                        .queryParam(PolicyController.PRICE , "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(20)))
                .andReturn();
        assertTrue("Merge", checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .queryParam(PolicyController.NAME_CONTAINS, "Silver")
                        .queryParam(PolicyController.PRICE , "100")
                        .queryParam(PolicyController.ORDERBY , "NAME"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(20)))
                .andReturn();
        assertTrue("Merge", checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

    }

    @Test
    public void testBetweenPrices() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .queryParam(PolicyController.PRICE_LESS_THAN , "300")
                        .queryParam(PolicyController.PRICE_MORE_THAN , "150")
                        .queryParam(PolicyController.ORDERBY , "PRICE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(10)))
                .andReturn();
        assertTrue("Merge", checkOrderByPrice(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .queryParam(PolicyController.PRICE_LESS_THAN , "300")
                        .queryParam(PolicyController.PRICE_MORE_THAN , "150")
                        .queryParam(PolicyController.ORDERBY , "NAME"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(10)))
                .andReturn();
        assertTrue("Merge", checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .queryParam(PolicyController.PRICE_LESS_THAN , "300")
                        .queryParam(PolicyController.PRICE_MORE_THAN , "150"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(10)))
                .andReturn();
        assertTrue("Merge", checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));
    }

    @Test
    public void testNameSearch() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .queryParam(PolicyController.NAME_CONTAINS, "Silver")
                        .queryParam(PolicyController.ORDERBY , "PRICE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(20)))
                .andReturn();
        assertTrue("Merge", checkOrderByPrice(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .queryParam(PolicyController.NAME_CONTAINS, "Silver")
                        .queryParam(PolicyController.ORDERBY , "NAME"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(20)))
                .andReturn();
        assertTrue("Merge", checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .queryParam(PolicyController.NAME_CONTAINS, "Silver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(20)))
                .andReturn();
        assertTrue("Merge", checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));
    }

    @Test
    public void testPriceSearch() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .queryParam(PolicyController.PRICE , "200")
                        .queryParam(PolicyController.ORDERBY , "NAME"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(10)))
                .andReturn();
        assertTrue("Merge", checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .queryParam(PolicyController.PRICE , "200")
                        .queryParam(PolicyController.ORDERBY , "PRICE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(10)))
                .andReturn();
        assertTrue("Merge", checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("FRONT_OFFICE"))
                        .queryParam(PolicyController.PRICE , "200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(10)))
                .andReturn();
        assertTrue("Merge", checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

    }

    @Test
    public void testOrderbyLowerCase() throws Exception {
        mockMvc.perform(get("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .queryParam(PolicyController.NAME_CONTAINS, "Silver")
                        .queryParam(PolicyController.ORDERBY , "price"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(20)));
        mockMvc.perform(get("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .queryParam(PolicyController.NAME_CONTAINS, "Silver")
                        .queryParam(PolicyController.ORDERBY , "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(20)));
    }

    private boolean checkOrderByPrice(PolicyView[] policies) {
        for (int i = 1; i < policies.length; i++) {
            if (policies[i].getPrice().compareTo(policies[i-1].getPrice()) == -1) return false;
        }
        return true;
    }

    private boolean checkOrderByName(PolicyView[] policies) {
        for (int i = 1; i < policies.length; i++) {
            if (policies[i].getName().compareTo(policies[i-1].getName()) == -1) return false;
        }
        return true;
    }


}
