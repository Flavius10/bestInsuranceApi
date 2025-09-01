package com.bestinsurance.api.domain.events;

import com.bestinsurance.api.domain.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

public class SubscriptionUpdated {


    private Subscription subscription;

    public SubscriptionUpdated(Subscription subscription){
        this.subscription = subscription;
    }

    public Subscription getSubscription(){
        return this.subscription;
    }

    public void setSubscription(final Subscription subscription){
        this.subscription = subscription;
    }
}
