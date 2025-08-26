package com.bestinsurance.api.rest;

import com.bestinsurance.api.domain.Policy;
import com.bestinsurance.api.dto.coverage.CoverageView;
import com.bestinsurance.api.dto.mappers.DTOMapper;
import com.bestinsurance.api.dto.policy.PolicyCreation;
import com.bestinsurance.api.dto.policy.PolicyUpdate;
import com.bestinsurance.api.dto.policy.PolicyView;
import com.bestinsurance.api.services.CrudService;
import com.bestinsurance.api.services.PoliciesService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/policies")
public class PolicyController extends AbstractSimpleIdCrudController<PolicyCreation, PolicyUpdate, PolicyView, Policy>{

    private static final Logger logger = LoggerFactory.getLogger(PolicyController.class);

    @Autowired
    private PoliciesService policiesService;

    @Override
    public CrudService<Policy, UUID> getService() {
        return this.policiesService;
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
