package com.bestinsurance.api.rest;

import com.bestinsurance.api.domain.Subscription;
import com.bestinsurance.api.domain.SubscriptionId;
import com.bestinsurance.api.dto.customer.CustomerView;
import com.bestinsurance.api.dto.mappers.DTOMapper;
import com.bestinsurance.api.dto.policy.PolicyView;
import com.bestinsurance.api.dto.subscription.SubscriptionCreation;
import com.bestinsurance.api.dto.subscription.SubscriptionUpdate;
import com.bestinsurance.api.dto.subscription.SubscriptionView;
import com.bestinsurance.api.services.CrudService;
import com.bestinsurance.api.services.SubscriptionService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController
        extends AbstractCrudController<SubscriptionCreation, SubscriptionUpdate, SubscriptionView, Subscription, SubscriptionId>{

    private static final String ID_CUSTOMER = "idCustomer";
    private static final String ID_POLICY = "idPolicy";

    @Autowired
    private SubscriptionService subscriptionService;

    @Override
    public CrudService<Subscription, SubscriptionId> getService(){
        return this.subscriptionService;
    }

    @GetMapping("/{"+ ID_CUSTOMER +"}/{" + ID_POLICY + "}")
    @Parameter(in = ParameterIn.PATH, name = ID_CUSTOMER, schema = @Schema(type = "string"), required = true)
    @Parameter(in = ParameterIn.PATH, name = ID_POLICY, schema = @Schema(type = "string"), required = true)
    @Override
    public SubscriptionView searchById(Map<String, String> id) {
        return super.searchById(id);
    }

    @PutMapping("/{"+ ID_CUSTOMER +"}/{" + ID_POLICY + "}")
    @Parameter(in = ParameterIn.PATH, name = ID_CUSTOMER, schema = @Schema(type = "string"), required = true)
    @Parameter(in = ParameterIn.PATH, name = ID_POLICY, schema = @Schema(type = "string"), required = true)
    @Override
    public SubscriptionView update(Map<String, String> id, SubscriptionUpdate dto) {
        return super.update(id, dto);
    }

    @DeleteMapping("/{"+ ID_CUSTOMER +"}/{" + ID_POLICY + "}")
    @Parameter(in = ParameterIn.PATH, name = ID_CUSTOMER, schema = @Schema(type = "string"), required = true)
    @Parameter(in = ParameterIn.PATH, name = ID_POLICY, schema = @Schema(type = "string"), required = true)
    @Override
    public void delete(Map<String, String> id) {
        super.delete(id);
    }

    @Override
    protected DTOMapper<SubscriptionCreation, Subscription> getCreateDtoMapper(){
        return (subscriptionCreation ) -> {
          Subscription subscription = new Subscription();

          SubscriptionId subscriptionId = new SubscriptionId();
          subscriptionId.setPolicyId(UUID.fromString(subscriptionCreation.getPolicyId()));
          subscriptionId.setCustomerId(UUID.fromString(subscriptionCreation.getCustomerId()));

          subscription.setId(subscriptionId);
          subscription.setPaidPrice(subscriptionCreation.getPrice());
          subscription.setStartDate(subscriptionCreation.getStartDate());
          subscription.setEndDate(subscriptionCreation.getEndDate());
          return subscription;
        };
    }

    @Override
    protected DTOMapper<SubscriptionUpdate, Subscription> getUpdateDtoMapper(){

        return (subscriptionUpdate) -> {
            Subscription subscription = new Subscription();
            subscription.setPaidPrice(subscriptionUpdate.getPrice());
            subscription.setStartDate(subscriptionUpdate.getStartDate());
            subscription.setEndDate(subscriptionUpdate.getEndDate());
            return subscription;
        };

    }

    @Override
    protected DTOMapper<Subscription, SubscriptionView> getSearchDtoMapper(){
        return (subscriptionInput) -> {

            SubscriptionView subscriptionView = new SubscriptionView();

            PolicyView policyView = new PolicyView();
            policyView.setId(subscriptionInput.getId().getPolicyId().toString());
            subscriptionView.setPolicyId(policyView);

            CustomerView customerView = new CustomerView();
            customerView.setId(subscriptionInput.getId().getCustomerId().toString());
            subscriptionView.setCustomerId(customerView);

            subscriptionView.setStartDate(subscriptionInput.getStartDate());
            subscriptionView.setEndDate(subscriptionInput.getEndDate());
            subscriptionView.setPrice(subscriptionInput.getPaidPrice());
            subscriptionView.setCreated(subscriptionInput.getCreated());
            subscriptionView.setUpdated(subscriptionInput.getUpdated());

            return subscriptionView;

        };
    }

    @Override
    protected DTOMapper<Map<String, String>, SubscriptionId> getIdDtoMapper(){
        return (idDtoMapper) -> {
            SubscriptionId subscriptionId = new SubscriptionId();
            subscriptionId.setCustomerId(UUID.fromString(idDtoMapper.get(ID_CUSTOMER)));
            subscriptionId.setPolicyId(UUID.fromString(idDtoMapper.get(ID_POLICY)));
            return subscriptionId;
        };
    }

}
