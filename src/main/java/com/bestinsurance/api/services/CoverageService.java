package com.bestinsurance.api.services;

import com.bestinsurance.api.domain.Coverage;
import com.bestinsurance.api.repos.CoverageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CoverageService extends AbstractCrudService<Coverage, UUID>{

    private static final Logger logger = LoggerFactory.getLogger(CoverageService.class);

    @Autowired
    private CoverageRepository coverageRepository;

    @Override
    public CrudRepository<Coverage, UUID> getRepository() {
        return this.coverageRepository;
    }
}
