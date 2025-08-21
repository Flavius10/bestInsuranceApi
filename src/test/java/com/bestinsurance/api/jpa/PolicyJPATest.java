package com.bestinsurance.api.jpa;

import com.bestinsurance.api.config.DomainConfig;
import com.bestinsurance.api.domain.Policy;
import com.bestinsurance.api.repos.PolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
/// Importa toate configuratile JPA din clasa adnotata cu Configuration
@Import(DomainConfig.class)
public class PolicyJPATest {

    @Autowired
    private PolicyRepository policyRepository;

    @Test
    public void testIdInitialize() {
        Policy policy = new Policy();

        policy.setName("Politica");
        policy.setPrice(BigDecimal.valueOf(1000.10));
        policy.setPoliciesCoverages(null);
        policy.setDescription("Descriere");

        Policy created_policy = policyRepository.save(policy);

        Assertions.assertNotNull(created_policy.getPolicy_id());
        Assertions.assertNotNull(created_policy.getCreated());
        Assertions.assertNotNull(created_policy.getUpdated());
    }

}
