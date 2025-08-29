package com.bestinsurance.api.rest;

import com.bestinsurance.api.domain.*;
import com.bestinsurance.api.dto.*;
import com.bestinsurance.api.dto.CountryView;
import com.bestinsurance.api.dto.customer.CustomerCreation;
import com.bestinsurance.api.dto.customer.CustomerUpdate;
import com.bestinsurance.api.dto.customer.CustomerView;
import com.bestinsurance.api.dto.mappers.DTOMapper;
import com.bestinsurance.api.dto.policy.PolicyView;
import com.bestinsurance.api.services.CrudService;
import com.bestinsurance.api.services.CustomerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/customers")
public class CustomerController extends AbstractSimpleIdCrudController<CustomerCreation, CustomerUpdate, CustomerView, Customer> {

    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String EMAIL = "email";
    public static final String AGE = "age";
    public final static String AGE_FROM = "ageFrom";
    public final static String AGE_TO = "ageTo";
    public static final String ORDERBY = "orderBy";
    public static final String ORDERDIRECTION = "orderDirection";
    private static final String ID = "id";
    private static final String DISCOUNT_PRICE = "discountedPrice";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerService customerService;

    @Override
    public CrudService<Customer, UUID> getService() {
        return this.customerService;
    }

    @Override
    @Parameter(in = ParameterIn.QUERY, name = NAME, schema = @Schema(type = "string"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = SURNAME, schema = @Schema(type = "string"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = EMAIL, schema = @Schema(type = "string"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = AGE_FROM, schema = @Schema(type = "string"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = AGE_TO, schema = @Schema(type = "string"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = ORDERBY, schema = @Schema(type = "string"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = ORDERDIRECTION, schema = @Schema(type = "string"), required = false)
    public List<CustomerView> all(Map<String, String> filters){

        try{
           String name = filters.get(NAME) == null ? null : filters.get(NAME);
           String surname = filters.get(SURNAME) == null ? null : filters.get(SURNAME);
           String email = filters.get(EMAIL) == null ? null : filters.get(EMAIL);

           Integer ageFrom = filters.get(AGE_FROM) == null ? null : Integer.parseInt(filters.get(AGE_FROM));
           Integer ageTo = filters.get(AGE_TO) == null ? null : Integer.parseInt(filters.get(AGE_TO));

           if (ageFrom != null && ageTo != null && ageTo < ageFrom){
               throw new IllegalArgumentException("ageTo cannot be less than ageFrom");
           }

           if (ageFrom == null || ageTo == null){
               throw new IllegalArgumentException("ageFrom or ageTo must be provided");
           }

           if (!(ageFrom == null && ageTo == null) && (ageFrom == null || ageTo == null)){
               throw new IllegalArgumentException("ageFrom and ageTo must be provided together");
           }

          String orderByParam = filters.get(ORDERBY);
          CustomerService.CustomerOrderBy orderBy;

            if (orderByParam != null){
                if (AGE.equalsIgnoreCase(orderByParam)) {
                    orderBy = CustomerService.CustomerOrderBy.birthDate;
                } else {
                    orderBy = CustomerService.CustomerOrderBy.valueOf(orderByParam.toLowerCase());
                }
            } else {
                orderBy = CustomerService.CustomerOrderBy.valueOf(NAME.toLowerCase());
            }

          CustomerService.OrderDirection orderDirection = filters.get(ORDERDIRECTION) == null ? null : CustomerService.OrderDirection.valueOf(filters.get(ORDERDIRECTION).toUpperCase());

          return this.customerService.findAllWithFilters(name, surname, email, ageFrom, ageTo, orderBy, orderDirection)
                   .stream().map(this.getSearchDtoMapper()::map).toList();

        } catch (Exception e){
            logger.error("Error during findAll: ", e);
            throw new RuntimeException("Error during search: " + e.getMessage(), e);
        }

    }

    @GetMapping("/policy/{" + ID + "}")
    @Parameter(in = ParameterIn.PATH, name = "id", schema = @Schema(type = "string"), required = true)
    public List<CustomerView> policies(@PathVariable Map<String, String> id){
        try{
            return this.customerService.selectCustomerByPolicy(this.getIdDtoMapper().map(id)).stream().map(this.getSearchDtoMapper()::map).toList();
        } catch (Exception e){
            logger.error("Error during findAll: ", e);
            throw new RuntimeException("Error during searchByPolicy: " + e.getMessage(), e);
        }
    }

    @GetMapping("/coverage/{" + ID + "}")
    @Parameter(in = ParameterIn.PATH, name = "id", schema = @Schema(type = "string"), required = true)
    public List<CustomerView> coverages(@PathVariable Map<String, String> id){
        try{
            return this.customerService.selectCustomerByCoverage(this.getIdDtoMapper().map(id)).stream().map(this.getSearchDtoMapper()::map).toList();
        } catch (Exception e){
            logger.error("Error during findAll: ", e);
            throw new RuntimeException("Error during searchByCoverage: " + e.getMessage(), e);
        }
    }

    @GetMapping("/subscriptions/{" + DISCOUNT_PRICE + "}")
    @Parameter(in = ParameterIn.PATH, name = DISCOUNT_PRICE, schema = @Schema(type = "string"), required = true)
    public List<CustomerView> subscriptions(){
        try{
            return this.customerService.selectCustomerByDiscountedPrice().stream().map(this.getSearchDtoMapper()::map).toList();
        } catch (Exception e){
            logger.error("Error during findAll: ", e);
            throw new RuntimeException("Error during searchByDiscountPrice: " + e.getMessage(), e);
        }
    }

    @GetMapping("/subscriptions")
    public List<CustomerView> dateMethodFilter(
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd") LocalDate endDate){
        try{
            return this.customerService.selectCustomerBySubscriptionDate(startDate, endDate).stream().map(this.getSearchDtoMapper()::map).toList();
        } catch (Exception e){
            logger.error("Error during findAll: ", e);
            throw new RuntimeException("Error during searchByDateTime: " + e.getMessage(), e);
        }
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
