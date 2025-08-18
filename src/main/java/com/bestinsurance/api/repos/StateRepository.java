package com.bestinsurance.api.repos;

import com.bestinsurance.api.domain.State;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface StateRepository extends CrudRepository<State, UUID> {
}
