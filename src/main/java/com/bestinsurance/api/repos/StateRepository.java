package com.bestinsurance.api.repos;

import com.bestinsurance.api.domain.State;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface StateRepository extends CrudRepository<State, UUID> {

    @Query("""
            SELECT DISTINCT(state) FROM State state
            JOIN FETCH state.stateCities c
            ORDER BY state.name, c.name ASC
            """)
    List<State> selectStatesAndCitiesOrdered();
}
