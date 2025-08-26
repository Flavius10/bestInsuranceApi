package com.bestinsurance.api.rest;

import com.bestinsurance.api.domain.Customer;
import com.bestinsurance.api.dto.mappers.DTOMapper;
import com.bestinsurance.api.services.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class AbstractCrudController<CreateDTO, UpdateDTO, SearchDTO, DomainObj, DomainObjId> implements
        CrudController<CreateDTO, UpdateDTO, SearchDTO>{

    private static final Logger logger = LoggerFactory.getLogger(AbstractCrudController.class);

    @Override
    public SearchDTO create(CreateDTO dto) {
        try {
            return this.getSearchDtoMapper().map(this.getService().create(this.getCreateDtoMapper().map(dto)));
        } catch (Exception e){
            logger.error("Error during creation: ", e);
            throw new RuntimeException("Error during create: " + e.getMessage(), e);
        }
    }

    @Override
    public SearchDTO searchById(Map<String, String> id){
        try {
            Optional<DomainObj> byId = this.getService().getById(this.getIdDtoMapper().map(id));
            if (byId.isPresent()) return this.getSearchDtoMapper().map(byId.get());
        } catch (Exception e){
            logger.error("Error during searchById: ", e);
            throw new RuntimeException("Error during search: " + e.getMessage(), e);
        }

        throw new NoSuchElementException("Object Not found on database");
    }

    @Override
    public SearchDTO update(Map<String, String> id, UpdateDTO dto) {
        try {
            return this.getSearchDtoMapper().map(this.getService().update(this.getIdDtoMapper().map(id), this.getUpdateDtoMapper().map(dto)));
        } catch (NoSuchElementException ne) {
            logger.error("Error during update: ", ne);
            throw ne;
        } catch (Exception e){
            logger.error("Error during update: ", e);
            throw new RuntimeException("Error during update: " + e.getMessage(), e);
        }
    }

    @Override
    public List<SearchDTO> all(){
        try {
            return this.getService().findAll().stream().map(this.getSearchDtoMapper()::map).toList();
        } catch (Exception e){
            logger.error("Error during findAll: ", e);
            throw new RuntimeException("Error during search: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Map<String, String> id) {
        try {
            this.getService().delete(this.getIdDtoMapper().map(id));
        } catch (NoSuchElementException ne) {
            logger.error("Error during delete: ", ne);
            throw ne;
        } catch (Exception e){
            logger.error("Error during delete: ", e);
            throw new RuntimeException("Error during delete: " + e.getMessage(), e);
        }
    }

    protected abstract CrudService<DomainObj, DomainObjId> getService();
    protected abstract DTOMapper<CreateDTO, DomainObj> getCreateDtoMapper();
    protected abstract DTOMapper<UpdateDTO, DomainObj> getUpdateDtoMapper();
    protected abstract DTOMapper<DomainObj, SearchDTO> getSearchDtoMapper();
    protected abstract DTOMapper<Map<String, String>, DomainObjId> getIdDtoMapper();
}
