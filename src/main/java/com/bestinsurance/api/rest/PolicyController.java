package com.bestinsurance.api.rest;

import com.bestinsurance.api.domain.Policy;
import com.bestinsurance.api.dto.coverage.CoverageView;
import com.bestinsurance.api.dto.mappers.DTOMapper;
import com.bestinsurance.api.dto.policy.PolicyCreation;
import com.bestinsurance.api.dto.policy.PolicyUpdate;
import com.bestinsurance.api.dto.policy.PolicyView;
import com.bestinsurance.api.services.CrudService;
import com.bestinsurance.api.services.PoliciesService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/policies")
public class PolicyController extends AbstractSimpleIdCrudController<PolicyCreation, PolicyUpdate, PolicyView, Policy>{

    private static final Logger logger = LoggerFactory.getLogger(PolicyController.class);

    public static final String NAME_CONTAINS = "nameContains";
    public static final String PRICE_MORE_THAN = "priceMoreThan";
    public static final String PRICE_LESS_THAN = "priceLessThan";
    public static final String PRICE = "price";
    public static final String ORDERBY = "orderBy";

    @Autowired
    private PoliciesService policiesService;

    @Override
    public CrudService<Policy, UUID> getService() {
        return this.policiesService;
    }

    @Override
    @Parameter(in = ParameterIn.QUERY, name = NAME_CONTAINS, schema = @Schema(type = "string"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = PRICE_MORE_THAN, schema = @Schema(type = "number"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = PRICE_LESS_THAN, schema = @Schema(type = "number"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = PRICE, schema = @Schema(type = "number"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = ORDERBY, schema = @Schema(type = "string"), required = false)
    public List<PolicyView> all(Map<String, String> filters){

        try{
            BigDecimal priceMoreThan = filters.get(PRICE_MORE_THAN) == null ? null : new BigDecimal(filters.get(PRICE_MORE_THAN));
            BigDecimal priceLessThan = filters.get(PRICE_LESS_THAN) == null ? null : new BigDecimal(filters.get(PRICE_LESS_THAN));
            BigDecimal price = filters.get(PRICE) == null ? null : new BigDecimal(filters.get(PRICE));
            PoliciesService.PolicyOrderBy orderBy = filters.get(ORDERBY) == null ? null : PoliciesService.PolicyOrderBy.valueOf(filters.get(ORDERBY).toUpperCase());
            String nameContains = filters.get(NAME_CONTAINS);

            return this.policiesService.findAllWitFilters(priceMoreThan, priceLessThan, price, nameContains, orderBy)
                    .stream().map(this.getSearchDtoMapper()::map).toList();
        } catch (Exception e) {
            logger.error("Error during findAll: ", e);
            throw new RuntimeException("Error during search: " + e.getMessage(), e);
        }
    }

    @Override
    public DTOMapper<PolicyCreation, Policy> getCreateDtoMapper(){
        return (policyCreation ) -> {
            Policy policy = new Policy();
            policy.setName(policyCreation.getName());
            policy.setPrice(policyCreation.getPrice());
            policy.setDescription(policyCreation.getDescription());
            return policy;
        };
    }

    @Override
    public DTOMapper<PolicyUpdate, Policy> getUpdateDtoMapper(){
        return (policyUpdate) -> {
            Policy policy = new Policy();
            policy.setName(policyUpdate.getName());
            policy.setDescription(policyUpdate.getDescription());
            policy.setPrice(policyUpdate.getPrice());
            return policy;
        };
    }

    @Override
    public DTOMapper<Policy, PolicyView> getSearchDtoMapper(){
        return (policyView ) -> {
            PolicyView policyViewDTO = new PolicyView();
            policyViewDTO.setId(policyView.getPolicy_id().toString());
            policyViewDTO.setName(policyView.getName());
            policyViewDTO.setDescription(policyView.getDescription());
            policyViewDTO.setPrice(policyView.getPrice());
            policyViewDTO.setCreated(policyView.getCreated());
            policyViewDTO.setUpdated(policyView.getUpdated());
            policyViewDTO.setCoverage(
                    policyView.getPoliciesCoverages() != null
                            ? policyView.getPoliciesCoverages().stream()
                            .map(coverage -> new CoverageView(
                                    coverage.getCoverage_id().toString(),
                                    coverage.getName(),
                                    coverage.getDescription()))
                            .collect(Collectors.toList())
                            : Collections.emptyList()
            );

            return policyViewDTO;
        };
    }
}