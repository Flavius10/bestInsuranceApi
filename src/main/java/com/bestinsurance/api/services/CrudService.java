package com.bestinsurance.api.services;

import com.bestinsurance.api.domain.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CrudService<DomainObj, DomainObjId> {

    DomainObj create(DomainObj domainObj);
    List<DomainObj> findAll();
    Optional<DomainObj> getById(DomainObjId id);
    DomainObj update(DomainObjId id, DomainObj domainObj);
    void delete(DomainObjId id);
}
