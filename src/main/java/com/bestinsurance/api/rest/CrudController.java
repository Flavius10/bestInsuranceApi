package com.bestinsurance.api.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// e interfata pentru ca sa pot sa creez un CRUD Controller pentru Customer, Subscription, Policy si Coverage
public interface CrudController<CreateDTO, UpdateDTO, SearchDTO> {

    @PostMapping
    SearchDTO create(@Valid @RequestBody CreateDTO create_dto);

    @GetMapping("/{id}")
    SearchDTO searchById(@NotBlank @PathVariable String id);

    @GetMapping
    List<SearchDTO> all();

    @PutMapping("/{id}")
    SearchDTO update(@NotBlank @PathVariable String id, @Valid @RequestBody UpdateDTO update_dto);

    @DeleteMapping("/{id}")
    void delete(@NotBlank @PathVariable String id);
}


