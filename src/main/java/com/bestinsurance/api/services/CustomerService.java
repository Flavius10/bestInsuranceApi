package com.bestinsurance.api.services;

import com.bestinsurance.api.domain.Customer;
import com.bestinsurance.api.repos.CustomerRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import org.slf4j.LoggerFactory;

import java.util.UUID;

@Service
public class CustomerService extends AbstractCrudService<Customer, UUID> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CrudRepository<Customer, UUID> getRepository() {
        return this.customerRepository;
    }

    @Override
    public void updatePreSave(Customer fetchedObj, Customer toSave){
        toSave.setName(fetchedObj.getName());
        toSave.setSurname(fetchedObj.getSurname());
    }
}
