package com.bestinsurance.api.services;

import com.bestinsurance.api.domain.Policy;
import com.bestinsurance.api.repos.PolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

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

}
