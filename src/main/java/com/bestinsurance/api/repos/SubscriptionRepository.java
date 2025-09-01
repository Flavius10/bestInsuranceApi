package com.bestinsurance.api.repos;

import com.bestinsurance.api.domain.StateSubscriptionRevenue;
import com.bestinsurance.api.domain.Subscription;
import com.bestinsurance.api.domain.SubscriptionId;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends CrudRepository<Subscription, SubscriptionId>, JpaSpecificationExecutor<Subscription> {

    @Query("""
            SELECT NEW com.bestinsurance.api.domain.StateSubscriptionRevenue(state.name, SUM(sub.paidPrice), COUNT(customer)) FROM Subscription sub
            JOIN sub.customer customer
            JOIN customer.address address
            JOIN address.state state
            GROUP BY state.state_id, state.name
            """)
    List<StateSubscriptionRevenue> selectStateRevenue();
}
