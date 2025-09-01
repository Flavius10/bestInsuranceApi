package com.bestinsurance.api.services;

import com.bestinsurance.api.domain.Customer;
import com.bestinsurance.api.repos.CustomerRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

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
        toSave.setBirthDate(fetchedObj.getBirthDate());
    }

    public enum CustomerOrderBy{
        name, surname, email, birthDate;
    }

    public enum OrderDirection{
        ASC, DESC;
    }

    public List<Customer> selectCustomerByPolicy(UUID policyId){
        return this.customerRepository.selectCustomerByPolicy(policyId);
    }

    public List<Customer> selectCustomerByCoverage(UUID coverageId){
        return this.customerRepository.selectCustomerByCoverage(coverageId);
    }

    public List<Customer> selectCustomerByDiscountedPrice(){
        return this.customerRepository.selectCustomerByDiscountedPrice();
    }

    public List<Customer> selectCustomerBySubscriptionDate(LocalDate startDate, LocalDate endDate){
        return this.customerRepository.selectCustomerBySubscriptionDate(startDate, endDate);
    }

    public List<Customer> findAllWithFilters(String nameContains, String surnameContains, String emailContains,
                                   Integer ageFrom, Integer ageTo, Integer pageNumber, Integer pageSize, CustomerOrderBy orderBy, OrderDirection orderDirection){

        Customer customer = new Customer();
        customer.setName(nameContains);
        customer.setSurname(surnameContains);
        customer.setEmail(emailContains);

        ExampleMatcher customMatchers = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("surname", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<Customer> exampleCustomerName = Example.of(customer, customMatchers);

        Sort sort;

        if (orderDirection == null){
            sort = Sort.by(Sort.Direction.ASC, orderBy.name());
        } else {
            if (orderDirection == OrderDirection.DESC) {
                sort = Sort.by(Sort.Direction.DESC, orderBy.name());
            } else {
                sort = Sort.by(Sort.Direction.ASC, orderBy.name());
            }
        }
        if (pageNumber == null){
            Iterable<Customer> customers = customerRepository.findAll(getSpecFromDatesAndExample(ageFrom, ageTo, exampleCustomerName), sort);
            List<Customer> customerList = new ArrayList<>();
            customers.forEach(customerList::add);
            return customerList;
        }
        else {
            PageRequest pageable = PageRequest.of(pageNumber, pageSize, sort);
            Iterable<Customer> customers = customerRepository.findAll(getSpecFromDatesAndExample(ageFrom, ageTo, exampleCustomerName), pageable);
            List<Customer> customerList = new ArrayList<>();
            customers.forEach(customerList::add);
            return customerList;
        }

    }

    private Specification<Customer> getSpecFromDatesAndExample(Integer ageFrom, Integer ageTo, Example<Customer> example){
        return (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (ageFrom != null && ageTo != null){
                LocalDate startDate = LocalDate.now().minusYears(ageTo);
                LocalDate endDate = LocalDate.now().minusYears(ageFrom);
                predicates.add(builder.between(root.get(CustomerOrderBy.birthDate.name()), startDate, endDate));
            }

            Predicate examplePredicate = QueryByExamplePredicateBuilder.getPredicate(root, builder, example);

            if (examplePredicate != null){
                predicates.add(examplePredicate);
            }

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
