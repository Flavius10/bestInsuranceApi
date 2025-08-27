package com.bestinsurance.api.repos;

import com.bestinsurance.api.domain.Policy;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.nio.Buffer;
import java.util.UUID;
import java.util.List;


/// Se poate ca sa creez metode care sa fie cu niste nume speciale carora Spring Data JPA sa le poata implementa
public interface PolicyRepository extends CrudRepository<Policy, UUID> {

    /// All the policies with a price over x
    List<Policy> findByPriceGreaterThanOrderByNameAsc(BigDecimal price);
    List<Policy> findByPriceGreaterThanOrderByPriceAsc(BigDecimal price);

    /// All the policies with a price less than x
    List<Policy> findByPriceLessThanOrderByPriceAsc(BigDecimal price);
    List<Policy> findByPriceLessThanOrderByNameAsc(BigDecimal price);

    /// All the policies with a price between x and y
    List<Policy> findByPriceGreaterThanAndPriceLessThanOrderByNameAsc(BigDecimal price1, BigDecimal price2);
    List<Policy> findByPriceGreaterThanAndPriceLessThanOrderByPriceAsc(BigDecimal price1, BigDecimal price2);

    /// All the policies with price equal to x
    List<Policy> findByPriceOrderByPriceAsc(BigDecimal price);
    List<Policy> findByPriceOrderByNameAsc(BigDecimal price);

    /// All the policies with a name containing a string
    List<Policy> findByNameContainingOrderByNameAsc(String name);
    List<Policy> findByNameContainingOrderByPriceAsc(String name);

    /// Order results by name and price (default by name, sort always ascending)
    List<Policy> findByPriceGreaterThanAndNameContainingOrderByNameAsc(BigDecimal price, String name);
    List<Policy> findByPriceGreaterThanAndNameContainingOrderByPriceAsc(BigDecimal price, String name);

    /// /policies?nameContains=string&priceMoreThan=x&priceLessThan=y&orderBy=["price"|name]
    List<Policy> findByNameContainingAndPriceGreaterThanAndPriceLessThanOrderByNameAsc(String name, BigDecimal price1, BigDecimal price2);
    List<Policy> findByNameContainingAndPriceGreaterThanAndPriceLessThanOrderByPriceAsc(String name, BigDecimal price1, BigDecimal price2);

    /// /policies?nameContains=string&price=x&orderBy=["price"|name]
    List<Policy> findByNameContainingAndPriceOrderByNameAsc(String name, BigDecimal price);
    List<Policy> findByNameContainingAndPriceOrderByPriceAsc(String name, BigDecimal price);

    /// /policies?priceLessThan=x&nameContains=string&orderBy=["price"|name]
    List<Policy> findByNameContainingAndPriceLessThanOrderByNameAsc(String name, BigDecimal price);
    List<Policy> findByNameContainingAndPriceLessThanOrderByPriceAsc(String name, BigDecimal price);

}
