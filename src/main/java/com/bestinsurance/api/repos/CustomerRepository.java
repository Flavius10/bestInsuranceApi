package com.bestinsurance.api.repos;

import com.bestinsurance.api.domain.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CustomerRepository extends CrudRepository<Customer, UUID>, JpaSpecificationExecutor<Customer>{
}