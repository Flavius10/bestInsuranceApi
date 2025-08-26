package com.bestinsurance.api.domain;

import java.util.UUID;

public interface DomainObject<DomainObjId> {
    void setId(DomainObjId id);
}
