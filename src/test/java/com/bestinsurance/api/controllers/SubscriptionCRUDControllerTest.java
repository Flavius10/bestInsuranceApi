package com.bestinsurance.api.controllers;

import com.bestinsurance.api.domain.*;
import com.bestinsurance.api.dto.subscription.SubscriptionCreation;
import com.bestinsurance.api.repos.CoverageRepository;
import com.bestinsurance.api.repos.CustomerRepository;
import com.bestinsurance.api.repos.PolicyRepository;
import com.bestinsurance.api.repos.SubscriptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.jms.TextMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubscriptionCRUDControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Value("${eventlistener.queue.name}")
    private String queueName;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private CoverageRepository coverageRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private MockMvc mockMvc;

    private Customer customer;
    private Policy policy;

    private Subscription subscription;


    @BeforeAll
    public void init() {
        om.registerModule(new JavaTimeModule());
        this.customer = createTestCustomer();
        this.policy = createTestPolicy("init");
        this.subscription = createSubscription(this.customer, this.policy);
    }

    @AfterAll
    public void cleanup() {
        this.subscriptionRepository.deleteAll();
        this.customerRepository.deleteAll();
        this.policyRepository.deleteAll();
    }

    @Test
    public void testCreateSubscription() throws Exception  {
        SubscriptionCreation subscriptionDTO = new SubscriptionCreation();
        subscriptionDTO.setCustomerId(this.customer.getCustomer_id().toString());

        Policy testPolicy = createTestPolicy("testCreate");
        subscriptionDTO.setPolicyId(testPolicy.getPolicy_id().toString());
        subscriptionDTO.setStartDate(LocalDate.now());
        subscriptionDTO.setEndDate(LocalDate.now().plusYears(1));
        subscriptionDTO.setPrice(new BigDecimal(100.00));
        MvcResult mvcResult = mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .content(om.writeValueAsString(subscriptionDTO)))
                .andDo(print())
                .andReturn();

        this.jmsTemplate.<TextMessage>browse(this.queueName, (session, browser) ->{
           Enumeration<?> browserEnumearation = browser.getEnumeration();
           while(browserEnumearation.hasMoreElements()){
               TextMessage message = (TextMessage) browserEnumearation.nextElement();
               assertTrue("Message is not correct", message.getText().contains("CREATE"));
           }
           return null;
        });

    }

    @Test
    public void testFindById() throws Exception {
        mockMvc.perform(get("/subscriptions/{idCustomer}/{idPolicy}", this.subscription.getCustomer().getCustomer_id().toString()
                        , this.subscription.getPolicy().getPolicy_id().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .queryParam("idCustomer", this.customer.getCustomer_id().toString())
                        .queryParam("idPolicy", this.policy.getPolicy_id().toString()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdate() throws Exception {
        LocalDate updateDate = LocalDate.now().plusYears(3);
        String formattedDateTime = updateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        SubscriptionCreation subscriptionDTO = new SubscriptionCreation();
        subscriptionDTO.setCustomerId(this.customer.getCustomer_id().toString());
        subscriptionDTO.setPolicyId(this.policy.getPolicy_id().toString());
        subscriptionDTO.setStartDate(LocalDate.now());
        subscriptionDTO.setEndDate(updateDate);
        subscriptionDTO.setPrice(new BigDecimal(150));

        mockMvc.perform(put("/subscriptions/{idCustomer}/{idPolicy}", this.subscription.getCustomer().getCustomer_id().toString()
                        , this.subscription.getPolicy().getPolicy_id().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .queryParam("idCustomer", this.customer.getCustomer_id().toString())
                        .queryParam("idPolicy", this.policy.getPolicy_id().toString())
                        .content(om.writeValueAsString(subscriptionDTO)));

        this.jmsTemplate.<TextMessage>browse(this.queueName, (session, browser) -> {
            Enumeration<?> browserEnumeration = browser.getEnumeration();
            while (browserEnumeration.hasMoreElements()) {
                TextMessage message = (TextMessage) browserEnumeration.nextElement();
                assertTrue("Message is not correct", message.getText().contains("UPDATE"));
            }
            return null;
        });
    }

    @Test
    public void testDelete() throws Exception {
        Policy testPolicy = createTestPolicy("testDElete");
        Subscription subscription1 = createSubscription(this.customer, testPolicy);
        mockMvc.perform(delete("/subscriptions/{idCustomer}/{idPolicy}", subscription1.getCustomer().getCustomer_id().toString()
                        , subscription1.getPolicy().getPoliciesCoverages().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("BACK_OFFICE"))
                        .queryParam("idCustomer", customer.getCustomer_id().toString())
                        .queryParam("idPolicy", testPolicy.getPolicy_id().toString()))
                .andDo(print());

        this.jmsTemplate.<TextMessage>browse(this.queueName, (session, browser) -> {
            Enumeration<?> browserEnumeration = browser.getEnumeration();
            while (browserEnumeration.hasMoreElements()) {
                TextMessage message = (TextMessage) browserEnumeration.nextElement();
                assertTrue("Message is not correct", message.getText().contains("DELETE"));
            }
            return null;
        });

    }

    private Customer createTestCustomer(){
        Customer customer = new Customer();
        customer.setName("testNameSubs");
        customer.setSurname("testSurnameSubs");
        customer.setEmail("testEmailSubs@email.com");
        customer.setBirthDate(LocalDate.now().minusYears(30));
        Address address = new Address();
        address.setAddress("123 Test Street, APT 4");
        address.setPostal_code("12345-44");
        Country country = new Country();
        country.setCountry_id(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        address.setCountry(country);
        State state = new State();
        state.setState_id(UUID.fromString("aaaaaaa1-0000-0000-0000-aaaaaaaaaaa1"));
        address.setState(state);
        City city = new City();
        city.setCity_id(UUID.fromString("45576d7c-8d84-4422-9440-19ef80fa16f3"));
        address.setCity(city);
        customer.setAddress(address);
        return customerRepository.save(customer);
    }

    private Policy createTestPolicy(String name) {
        Set<Coverage> coverages = new LinkedHashSet<>();
        for (int i=0; i <= 2; i++) {
            Coverage coverage = new Coverage();
            coverage.setName("testCoverage" + i);
            coverage.setDescription("subs coverage description " + i);
            coverages.add(coverageRepository.save(coverage));
        }

        Policy policy = new Policy();
        policy.setName(name);
        policy.setDescription("policy test subscriptions description");
        policy.setPrice(new BigDecimal(100.50));
        policy.setPoliciesCoverages(coverages);

        return policyRepository.save(policy);
    }

    private Subscription createSubscription(Customer c, Policy p) {
        SubscriptionId id = new SubscriptionId();
        id.setCustomerId(c.getCustomer_id());
        id.setPolicyId(p.getPolicy_id());
        Subscription subscription = new Subscription();
        subscription.setId(id);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusYears(1));
        subscription.setPaidPrice(new BigDecimal(100.00));
        subscription.setCustomer(c);
        subscription.setPolicy(p);
        return subscriptionRepository.save(subscription);
    }

}