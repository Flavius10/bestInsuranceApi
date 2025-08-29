package com.bestinsurance.api.jpa;

import com.bestinsurance.api.domain.City;
import com.bestinsurance.api.domain.Customer;
import com.bestinsurance.api.repos.CityRepository;
import com.bestinsurance.api.repos.CustomerRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static com.bestinsurance.api.jpa.PersistenceEntitiesUtil.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractCustomerInitializedTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CityRepository cityRepository;

    @BeforeAll
    public void initD(){
        this.cleanDB();
        LocalDate[] ages = {LocalDate.now().minusYears(20),
                LocalDate.now().minusYears(40),
                LocalDate.now().minusYears(50),
                LocalDate.now().minusYears(60),
                LocalDate.now().minusYears(70)};

        String[] cityIds = {"45576d7c-8d84-4422-9440-19ef80fa16f3",
                "91f360d5-811b-417c-a202-f5ba4b34b895",
                "144b05b6-ebf6-43a8-836d-0998c2c20a3c",
                "74716a04-d538-4441-84bf-7c41470778ca",
                "eb5e9505-8580-4857-9195-6bee0324ac0f"};

        for (int i = 0; i < 10; i++) {
            char namePrefix = (char)('A' + i);
            for(int j = 0; j < cityIds.length; j++) { //CREATE 5 customers for each prefix
                save(instanceCustomer(namePrefix + "customerName", namePrefix + "customerSurname", namePrefix + "N" + j + "@customer.com",
                        ages[j], instanceAddress("street test " + 1, "12345" + i, findCity(cityIds[j]))));
            }
        }
    }

    @AfterAll
    public void cleanDB() {
        customerRepository.deleteAll();
    }

    private void save(Customer customer){
        this.customerRepository.save(customer);
    }

    private City findCity(String cityId){
        return this.cityRepository.findById(UUID.fromString(cityId)).get();
    }

}
