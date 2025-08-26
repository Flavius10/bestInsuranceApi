package com.bestinsurance.api.dto.policy;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;

public class PolicyUpdate {

    @NotBlank
    @Length(max = 16)
    private String name;

    @NotBlank
    @Length(max = 2048)
    private String description;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    private BigDecimal price;

    @NotNull
    @NotEmpty
    private List<String> coveragesIds;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public List<String> getCoveragesIds() {
        return coveragesIds;
    }

    public void setCoveragesIds(final List<String> coveragesIds) {
        this.coveragesIds = coveragesIds;
    }

}
