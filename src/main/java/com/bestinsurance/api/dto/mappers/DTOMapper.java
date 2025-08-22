package com.bestinsurance.api.dto.mappers;

public interface DTOMapper<Input, Output> {
    Output map(Input input);
}
