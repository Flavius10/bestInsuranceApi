package com.bestinsurance.api.services;

import com.bestinsurance.api.domain.Customer;
import com.bestinsurance.api.repos.CustomerRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService implements CrudService<Customer>{

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    public CustomerRepository customerRepository;

    @Override
    public Customer create(Customer customer){
        return this.customerRepository.save(customer);
    }

    @Override
    public List<Customer> findAll(){
        return (List<Customer>)this.customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getById(UUID id){
        return this.customerRepository.findById(id);
    }

    @Override
    public Customer update(UUID id, Customer customer){

        Customer current_customer = this.customerRepository.findById(id).get();
        Optional<Customer> customerOptional = this.customerRepository.findById(id);

        if (customerOptional.isPresent()){
            current_customer.setEmail(customer.getEmail());
            current_customer.setTelephone_number(customer.getTelephone_number());
            current_customer.setAddress(customer.getAddress());
            current_customer.setCustomerSubscriptions(customer.getCustomerSubscriptions());

            return this.customerRepository.save(current_customer);
        }
        else {
            logger.error("Customer with id " + id + " not found");
            throw new NoSuchElementException("Customer with id " + id + " not found");
        }
    }

    @Override
    public void delete(UUID id){
        Optional<Customer> customerOptional = this.customerRepository.findById(id);

        if (customerOptional.isPresent()){
            this.customerRepository.deleteById(id);
        }
        else {
            logger.error("Customer with id " + id + " not found");
            throw new NoSuchElementException("Customer with id " + id + " not found");
        }
    }

}
