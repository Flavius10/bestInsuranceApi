package com.bestinsurance.api.config;

import java.time.OffsetDateTime;
import java.util.Optional;

import com.bestinsurance.api.repos.*;
import com.bestinsurance.api.services.SampleDataLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("com.bestinsurance.api.domain")
@EnableJpaRepositories("com.bestinsurance.api.repos")
@EnableTransactionManagement
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class DomainConfig {

    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }

    @Bean
    @DependsOn("liquibase")
    @ConditionalOnProperty(prefix = "dataloader", name = "loadsample", havingValue = "true")
    public SampleDataLoader sampleDataLoader(
            CustomerRepository customerRepository,
            CityRepository cityRepository,
            CoverageRepository coverageRepository,
            PolicyRepository policyRepository,
            SubscriptionRepository subscriptionRepository) {

        return new SampleDataLoader(
                customerRepository,
                cityRepository,
                coverageRepository,
                policyRepository,
                subscriptionRepository
        );
    }
}