package com.bestinsurance.api.rest;

import com.bestinsurance.api.domain.*;
import com.bestinsurance.api.dto.*;
import com.bestinsurance.api.dto.mappers.DTOMapper;
import com.bestinsurance.api.services.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController implements CrudController<CustomerCreation, CustomerUpdate, CustomerView> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerService customerService;

    @Override
    public CustomerView create(CustomerCreation dto) {
        try {
            return this.getSearchDtoMapper().map(this.customerService.create(this.getCreateDtoMapper().map(dto)));
        } catch (Exception e){
            logger.error("Error during creation: ", e);
            throw new RuntimeException("Error during create: " + e.getMessage(), e);
        }
    }

    @Override
    public CustomerView searchById(String id)  {
        try {
            Optional<Customer> byId = this.customerService.getById(UUID.fromString(id));
            if (byId.isPresent()) return this.getSearchDtoMapper().map(byId.get());
        } catch (Exception e){
            logger.error("Error during searchById: ", e);
            throw new RuntimeException("Error during search: " + e.getMessage(), e);
        }

        throw new NoSuchElementException("Object Not found on database");
    }

    @Override
    public List<CustomerView> all()  {
        try {
            return this.customerService.findAll().stream().map(this.getSearchDtoMapper()::map).toList();
        } catch (Exception e){
            logger.error("Error during findAll: ", e);
            throw new RuntimeException("Error during search: " + e.getMessage(), e);
        }
    }

    @Override
    public CustomerView update(String id, CustomerUpdate dto) {
        try {
            return this.getSearchDtoMapper().map(this.customerService.update(UUID.fromString(id), this.getUpdateDtoMapper().map(dto)));
        } catch (NoSuchElementException ne) {
            logger.error("Error during update: ", ne);
            throw ne;
        } catch (Exception e){
            logger.error("Error during update: ", e);
            throw new RuntimeException("Error during update: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            this.customerService.delete(UUID.fromString(id));
        } catch (NoSuchElementException ne) {
            logger.error("Error during delete: ", ne);
            throw ne;
        } catch (Exception e){
            logger.error("Error during delete: ", e);
            throw new RuntimeException("Error during delete: " + e.getMessage(), e);
        }
    }

    private DTOMapper<CustomerCreation, Customer> getCreateDtoMapper() {
        return (customerCreationDTO) -> {
            Customer customer = new Customer();
            customer.setName(customerCreationDTO.getName());
            customer.setSurname(customerCreationDTO.getSurname());
            customer.setEmail(customerCreationDTO.getEmail());
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

    private DTOMapper<CustomerUpdate, Customer> getUpdateDtoMapper() {
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

    private DTOMapper<Customer, CustomerView> getSearchDtoMapper() {
        return (customer) -> {
            CustomerView customerViewDTO = new CustomerView();
            customerViewDTO.setId(customer.getCustomer_id().toString());
            customerViewDTO.setName(customer.getName());
            customerViewDTO.setSurname(customer.getSurname());
            customerViewDTO.setEmail(customer.getEmail());

            AddressView addressDTO = new AddressView();
            addressDTO.setId(customer.getAddress().getAddress_id().toString());
            addressDTO.setAddress(customer.getAddress().getAddress());
            addressDTO.setPostal_code(customer.getAddress().getPostal_code());
            Address address = customer.getAddress();
            addressDTO.setCountry(new CountryView(address.getCountry().getCountry_id().toString(),
                    address.getCountry().getName()));
            addressDTO.setState(new StateView(address.getState().getState_id().toString(),
                    address.getState().getName()));
            addressDTO.setCity(new CityView(address.getCity().getCity_id().toString(), address.getCity().getName()));

            customerViewDTO.setAddress(addressDTO);
            return customerViewDTO;
        };
    }

}
