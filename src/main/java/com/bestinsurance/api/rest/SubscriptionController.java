package com.bestinsurance.api.rest;

import com.bestinsurance.api.domain.StateSubscriptionRevenue;
import com.bestinsurance.api.domain.Subscription;
import com.bestinsurance.api.domain.SubscriptionId;
import com.bestinsurance.api.dto.CsvSubscriptions;
import com.bestinsurance.api.dto.customer.CustomerView;
import com.bestinsurance.api.dto.mappers.DTOMapper;
import com.bestinsurance.api.dto.policy.PolicyView;
import com.bestinsurance.api.dto.subscription.SubscriptionCreation;
import com.bestinsurance.api.dto.subscription.SubscriptionRevenueView;
import com.bestinsurance.api.dto.subscription.SubscriptionUpdate;
import com.bestinsurance.api.dto.subscription.SubscriptionView;
import com.bestinsurance.api.services.CrudService;
import com.bestinsurance.api.services.CsvService;
import com.bestinsurance.api.services.SubscriptionService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/subscriptions")
@SecurityRequirement(name = "security_auth")
public class SubscriptionController
        extends AbstractCrudController<SubscriptionCreation, SubscriptionUpdate, SubscriptionView, Subscription, SubscriptionId>{

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    private static final String ID_CUSTOMER = "idCustomer";
    private static final String ID_POLICY = "idPolicy";

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private CsvService csvService;

    @Override
    public CrudService<Subscription, SubscriptionId> getService(){
        return this.subscriptionService;
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String uploadSubscriptions(@RequestParam(value = "file") MultipartFile file) throws Exception{
        try{
            Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                CsvToBean<CsvSubscriptions> csvToBean = new CsvToBeanBuilder<CsvSubscriptions>(reader)
                        .withType(CsvSubscriptions.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withSeparator(';')
                        .build();
                List<CsvSubscriptions> csvSubscriptions = csvToBean.parse();
            this.csvService.save(csvSubscriptions);
        } catch (Exception ex){
            logger.info("Error during uploadSubscriptions: ", ex);
            throw new RuntimeException("Error during uploadSubscriptions: " + ex.getMessage(), ex);
        }

        return "File uploaded successfully";
    }

    @GetMapping("/revenues")
    public List<SubscriptionRevenueView> setStateSubscriptionRevenue(){
        try{
            return this.subscriptionService.selectStateSubscriptionsRevenue().stream().
                    map(x -> new SubscriptionRevenueView(x.name(), x.revenue(), x.customerCount())).toList();
        } catch (Exception ex){
            logger.info("Error during setStateSubscriptionRevenue: ", ex);
            throw new RuntimeException("Error during setStateSubscriptionRevenue: " + ex.getMessage(), ex);
        }
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
