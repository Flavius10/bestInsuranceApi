package com.bestinsurance.api.rest;

import com.bestinsurance.api.domain.Coverage;
import com.bestinsurance.api.dto.coverage.CoverageCreation;
import com.bestinsurance.api.dto.coverage.CoverageUpdate;
import com.bestinsurance.api.dto.coverage.CoverageView;
import com.bestinsurance.api.dto.mappers.DTOMapper;
import com.bestinsurance.api.services.CoverageService;
import com.bestinsurance.api.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/coverages")
public class CoverageController extends AbstractSimpleIdCrudController<CoverageCreation, CoverageUpdate, CoverageView, Coverage>{
    @Autowired
    private CoverageService coverageService;

    @Override
    protected CrudService<Coverage, UUID> getService() {
        return this.coverageService;
    }

    @Override
    protected DTOMapper<CoverageCreation, Coverage> getCreateDtoMapper() {
        return (dto) -> {
            Coverage entity = new Coverage();
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            return entity;
        };
    }

    @Override
    protected DTOMapper<CoverageUpdate, Coverage> getUpdateDtoMapper() {
        return (dto) -> {
            Coverage entity = new Coverage();
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            return entity;
        };
    }

    @Override
    protected DTOMapper<Coverage, CoverageView> getSearchDtoMapper(){
        return (coverageDto) -> {
            CoverageView coverageView = new CoverageView(coverageDto.getCoverage_id().toString(), coverageDto.getName(), coverageDto.getDescription());
            return coverageView;
        };
    }


}
