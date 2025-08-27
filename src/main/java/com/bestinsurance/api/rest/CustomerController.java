package com.bestinsurance.api.rest;

import com.bestinsurance.api.domain.*;
import com.bestinsurance.api.dto.*;
import com.bestinsurance.api.dto.CountryView;
import com.bestinsurance.api.dto.customer.CustomerCreation;
import com.bestinsurance.api.dto.customer.CustomerUpdate;
import com.bestinsurance.api.dto.customer.CustomerView;
import com.bestinsurance.api.dto.mappers.DTOMapper;
import com.bestinsurance.api.services.CrudService;
import com.bestinsurance.api.services.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/customers")
public class CustomerController extends AbstractSimpleIdCrudController<CustomerCreation, CustomerUpdate, CustomerView, Customer> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerService customerService;

    @Override
    public CrudService<Customer, UUID> getService() {
        return this.customerService;
    }

    @Override
    protected DTOMapper<CustomerCreation, Customer> getCreateDtoMapper() {
        return (customerCreationDTO) -> {
            Customer customer = new Customer();
            customer.setName(customerCreationDTO.getName());
            customer.setSurname(customerCreationDTO.getSurname());
            customer.setEmail(customerCreationDTO.getEmail());
            customer.setBirthDate(customerCreationDTO.getBirthDate());
            if (!StringUtils.isBlank(customerCreationDTO.getAddress())) {
                Address address = new Address();
                address.setAddress(customerCreationDTO.getAddress());
                address.setPostal_code(customerCreationDTO.getPostal_code());
                Country country = new Country();
                country.setCountry_id(UUID.fromString(customerCreationDTO.getIdCountry()));
                address.setCountry(country);
                if (!StringUtils.isBlank(customerCreationDTO.getIdState())) {
                    State state = new State();
                    state.setState_id(UUID.fromString(customerCreationDTO.getIdState()));
                    address.setState(state);
                }
                City city = new City();
                city.setCity_id(UUID.fromString(customerCreationDTO.getIdCity()));
                address.setCity(city);

                customer.setAddress(address);
            }
            return customer;
        };
    }

    @Override
    protected DTOMapper<CustomerUpdate, Customer> getUpdateDtoMapper() {
        return (customerUpdateDTO) -> {
            Customer customer = new Customer();
            customer.setEmail(customerUpdateDTO.getEmail());
            if (!StringUtils.isBlank(customerUpdateDTO.getAddress())) {
                Address address = new Address();
                address.setAddress(customerUpdateDTO.getAddress());
                address.setPostal_code(customerUpdateDTO.getPostal_code());
                Country country = new Country();
                country.setCountry_id(UUID.fromString(customerUpdateDTO.getIdCountry()));
                address.setCountry(country);
                if (!StringUtils.isBlank(customerUpdateDTO.getIdState())) {
                    State state = new State();
                    state.setState_id(UUID.fromString(customerUpdateDTO.getIdState()));
                    address.setState(state);
                }
                City city = new City();
                city.setCity_id(UUID.fromString(customerUpdateDTO.getIdCity()));
                address.setCity(city);

                customer.setAddress(address);
            }
            return customer;
        };
    }

    @Override
    protected DTOMapper<Customer, CustomerView> getSearchDtoMapper() {
        return (customer) -> {
            CustomerView customerViewDTO = new CustomerView();
            customerViewDTO.setId(customer.getCustomer_id().toString());
            customerViewDTO.setName(customer.getName());
            customerViewDTO.setSurname(customer.getSurname());
            customerViewDTO.setEmail(customer.getEmail());
            customerViewDTO.setBirthDate(customer.getBirthDate());

            if (customer.getAddress() != null){

                AddressView addressDTO = new AddressView();
                addressDTO.setId(customer.getAddress().getAddress_id().toString());
                addressDTO.setAddress(customer.getAddress().getAddress());
                addressDTO.setPostal_code(customer.getAddress().getPostal_code());
                Address address = customer.getAddress();

                if (address.getCountry() != null){
                    addressDTO.setCountry(new CountryView(address.getCountry().getCountry_id().toString(),
                            address.getCountry().getName()));
                }

                if (address.getState() != null){
                    addressDTO.setState(new StateView(address.getState().getState_id().toString(),
                            address.getState().getName()));
                }

                if (address.getCity() != null){
                    addressDTO.setCity(new CityView(address.getCity().getCity_id().toString(), address.getCity().getName()));
                }

                customerViewDTO.setAddress(addressDTO);
            }

            return customerViewDTO;
        };
    }

}
