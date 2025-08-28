package com.bestinsurance.api.services;

import com.bestinsurance.api.domain.Policy;
import com.bestinsurance.api.repos.PolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class PoliciesService extends AbstractCrudService<Policy, UUID> {

    private static final Logger logger = LoggerFactory.getLogger(PoliciesService.class);

    @Autowired
    private PolicyRepository policyRepository;

    @Override
    public CrudRepository<Policy, UUID> getRepository() {
        return this.policyRepository;
    }

    @Override
    public void updatePreSave(Policy fetchedObj, Policy toSave){
        super.updatePreSave(fetchedObj, toSave);
        toSave.setName(fetchedObj.getName());
        toSave.setDescription(fetchedObj.getDescription());
    }

    public enum PolicyOrderBy{
        NAME, PRICE;
    }

    public List<Policy> findAllWitFilters(BigDecimal priceMoreThan, BigDecimal priceLessThan, BigDecimal price,
                                          String nameContains, PolicyOrderBy orderBy){

        if (priceMoreThan != null && priceLessThan != null){
            if (nameContains != null){
                if (orderBy == PolicyOrderBy.PRICE){
                    return this.policyRepository.findByNameContainingAndPriceGreaterThanAndPriceLessThanOrderByPriceAsc(nameContains, priceMoreThan, priceLessThan);
                }
                return this.policyRepository.findByNameContainingAndPriceGreaterThanAndPriceLessThanOrderByNameAsc(nameContains, priceMoreThan, priceLessThan);
            }
            else {
                if (orderBy == PolicyOrderBy.PRICE){
                    return this.policyRepository.findByPriceGreaterThanAndPriceLessThanOrderByPriceAsc(priceMoreThan, priceLessThan);
                }
                return this.policyRepository.findByPriceGreaterThanAndPriceLessThanOrderByNameAsc(priceMoreThan, priceLessThan);
            }
        }

        if (priceMoreThan != null){
            if(nameContains != null){
                if (orderBy == PolicyOrderBy.PRICE){
                    return this.policyRepository.findByNameContainingAndPriceGreaterThanOrderByPriceAsc(nameContains, priceMoreThan);
                }
                return this.policyRepository.findByNameContainingAndPriceGreaterThanOrderByNameAsc(nameContains, priceMoreThan);
            } else {
                if (orderBy == PolicyOrderBy.PRICE){
                    return this.policyRepository.findByPriceGreaterThanOrderByPriceAsc(priceMoreThan);
                }
                return this.policyRepository.findByPriceGreaterThanOrderByNameAsc(priceMoreThan);
            }
        }

        if (priceLessThan != null){
            if(nameContains != null){
                if (orderBy == PolicyOrderBy.PRICE){
                    return this.policyRepository.findByNameContainingAndPriceLessThanOrderByPriceAsc(nameContains, priceLessThan);
                }
                return this.policyRepository.findByNameContainingAndPriceLessThanOrderByNameAsc(nameContains, priceLessThan);
            } else {
                if (orderBy == PolicyOrderBy.PRICE){
                    return this.policyRepository.findByPriceLessThanOrderByPriceAsc(priceLessThan);
                }
                return this.policyRepository.findByPriceLessThanOrderByNameAsc(priceLessThan);
            }
        }

        if (nameContains != null){
            if (orderBy == PolicyOrderBy.PRICE){
                return this.policyRepository.findByNameContainingOrderByPriceAsc(nameContains);
            }
            return this.policyRepository.findByNameContainingOrderByNameAsc(nameContains);
        }

        if (price != null){
            if (orderBy == PolicyOrderBy.PRICE){
                return this.policyRepository.findByPriceOrderByPriceAsc(price);
            }
            return this.policyRepository.findByPriceOrderByNameAsc(price);
        }

        return this.findAll();

    }

}
