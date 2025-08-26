package com.bestinsurance.api.rest;

import com.bestinsurance.api.dto.mappers.DTOMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.yaml.snakeyaml.events.Event;

import java.util.Map;
import java.util.UUID;

public abstract class AbstractSimpleIdCrudController<CreateDTO, UpdateDTO, SearchDTO, DomainObj>
        extends AbstractCrudController<CreateDTO, UpdateDTO, SearchDTO, DomainObj, UUID>{

    private final String ID = "id";

    @GetMapping("/{" + ID + "}")
    @Parameter(in = ParameterIn.PATH, name = ID, schema = @Schema(type="string"), required = true)
    @Override
    public SearchDTO searchById(Map<String, String> id){
        return super.searchById(id);
    }

    @PutMapping("/{" + ID + "}")
    @Parameter(in = ParameterIn.PATH, name = ID, schema = @Schema(type="string"), required = true)
    @Override
    public SearchDTO update(Map<String, String> id, UpdateDTO dto){
        return super.update(id, dto);
    }

    @DeleteMapping("/{" + ID + "}")
    @Parameter(in = ParameterIn.PATH, name = ID, schema = @Schema(type="string"), required = true)
    @Override
    public void delete(Map<String, String> id){
        super.delete(id);
    }

    @Override
    public DTOMapper<Map<String, String>, UUID> getIdDtoMapper(){
        return(idDtoMapper) -> {
            return UUID.fromString(idDtoMapper.get(ID));
        };
    }

}
