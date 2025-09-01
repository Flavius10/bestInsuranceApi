package com.bestinsurance.api.services;

import com.bestinsurance.api.domain.*;
import com.bestinsurance.api.dto.CsvSubscriptions;
import com.bestinsurance.api.repos.*;
import com.opencsv.bean.CsvDate;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CsvService {

    private static final Logger logger = LoggerFactory.getLogger(CsvService.class);

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Transactional
    public void save(List<CsvSubscriptions> csvCustomers){
        for (CsvSubscriptions csvCustomer : csvCustomers) {

            Policy policy = policyRepository.findByName(csvCustomer.getPolicyName());
            if (policy == null) {
                throw new NoSuchElementException("Policy " + csvCustomer.getPolicyName() + " not found");
            }
            City city = cityRepository.findByCityNameStateName(csvCustomer.getCityName(), csvCustomer.getStateName());
            if (city == null) {
                throw new NoSuchElementException("City: " + csvCustomer.getCityName() + " State: " + csvCustomer.getStateName() + " not found");
            }

            try{
               Optional<Customer> byEmail = this.customerRepository.findByEmail(csvCustomer.getEmail());
               Customer savedCustomer;

               if (byEmail.isPresent()){
                   savedCustomer = this.updateCustomer(byEmail.get(), byEmail.get().getAddress(), city, csvCustomer);
               }else{
                   Address address = new Address();
                   Customer customer = new Customer();
                   customer.setAddress(address);
                   savedCustomer = this.updateCustomer(customer, address, city, csvCustomer);
               }


               SubscriptionId id = new SubscriptionId();
               id.setCustomerId(savedCustomer.getCustomer_id());
               id.setPolicyId(policy.getPolicy_id());

                Subscription subscription = new Subscription();

                subscription.setId(id);
                subscription.setPaidPrice(csvCustomer.getPrice());
                subscription.setCustomer(savedCustomer);
                subscription.setPolicy(policy);

                subscription.setStartDate(csvCustomer.getStartDate());
                subscription.setEndDate(csvCustomer.getEndDate());


                this.subscriptionRepository.save(subscription);
            } catch (Exception e){
                logger.info("Could not find customer with email: ", csvCustomer.getEmail());
                throw new NoSuchElementException("Could not find customer with email: " + csvCustomer.getEmail());
            }
        }
    }

    public Customer updateCustomer(Customer customer, Address address, City city, CsvSubscriptions csvCustomer){
        address.setPostal_code(csvCustomer.getPostal_code());
        address.setAddress(csvCustomer.getAddress());
        address.setCountry(city.getCountry());
        address.setState(city.getState());
        address.setCity(city);
        customer.setName(csvCustomer.getName());
        customer.setEmail(csvCustomer.getEmail());
        customer.setSurname(csvCustomer.getSurname());
        customer.setBirthDate(csvCustomer.getBirthDate());
        return customerRepository.save(customer);
    }

}
