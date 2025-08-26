package com.bestinsurance.api.services;

import com.bestinsurance.api.domain.Subscription;
import com.bestinsurance.api.domain.SubscriptionId;
import com.bestinsurance.api.repos.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService extends AbstractCrudService<Subscription, SubscriptionId>{

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public CrudRepository<Subscription, SubscriptionId> getRepository() {
        return this.subscriptionRepository;
    }

    @Override
    public void updatePreSave(Subscription fetchedObj, Subscription toSave){
        super.updatePreSave(fetchedObj, toSave);
        toSave.setPolicy(fetchedObj.getPolicy());
        toSave.setCustomer(fetchedObj.getCustomer());
    }

}
