package com.bestinsurance.api.repos;

import com.bestinsurance.api.domain.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends CrudRepository<Customer, UUID>, JpaSpecificationExecutor<Customer>{

    @Query(""" 
            SELECT c FROM Customer c 
            JOIN c.customerSubscriptions s
            JOIN s.policy p
            WHERE p.policy_id = :policyId
            ORDER BY c.name """)
    List<Customer> selectCustomerByPolicy(UUID policyId);


    @Query("""
            SELECT c FROM Customer c
            JOIN c.customerSubscriptions s
            JOIN s.policy p
            JOIN p.coverages co
            WHERE co.coverage_id = :coverageId
            ORDER BY c.name
            """)
    List<Customer> selectCustomerByCoverage(UUID coverageId);

    @Query("""
           SELECT c FROM Customer c
           JOIN c.customerSubscriptions s
           JOIN s.policy p
           WHERE s.paidPrice < p.price
           ORDER BY c.name
           """)
    List<Customer> selectCustomerByDiscountedPrice();


    @Query("""
           SELECT c FROM Customer c
           JOIN c.customerSubscriptions s
           WHERE s.startDate >= :startDate AND s.endDate <= :endDate
           ORDER BY c.name
           """)
    List<Customer> selectCustomerBySubscriptionDate(LocalDate startDate, LocalDate endDate);
}