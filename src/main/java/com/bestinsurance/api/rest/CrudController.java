package com.bestinsurance.api.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/// e interfata pentru ca sa pot sa creez un CRUD Controller pentru Customer, Subscription, Policy si Coverage
public interface CrudController<CreateDTO, UpdateDTO, SearchDTO> {

    @PostMapping
    SearchDTO create(@Valid @RequestBody CreateDTO create_dto);

    SearchDTO searchById(@NotEmpty @PathVariable Map<String, String> id);

    @GetMapping
    List<SearchDTO> all();

    SearchDTO update(@NotEmpty @PathVariable Map<String, String> id, @Valid @RequestBody UpdateDTO update_dto);

    void delete(@NotEmpty @PathVariable Map<String, String> id);
}


