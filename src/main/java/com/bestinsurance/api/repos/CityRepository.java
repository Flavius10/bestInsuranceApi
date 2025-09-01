package com.bestinsurance.api.repos;

import com.bestinsurance.api.domain.City;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CityRepository extends CrudRepository<City,UUID> {

    @Query("""
            SELECT c FROM City c
            WHERE c.name = :cityName AND c.state.name = :stateName
            """)
    City findByCityNameStateName(String cityName, String stateName);
}
