package com.bestinsurance.api.dto.policy;

import com.bestinsurance.api.dto.coverage.CoverageView;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class PolicyView {

    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private OffsetDateTime created;
    private OffsetDateTime updated;
    private List<CoverageView> coverage;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

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

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(final OffsetDateTime created) {
        this.created = created;
    }

    public OffsetDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(final OffsetDateTime updated) {
        this.updated = updated;
    }

    public List<CoverageView> getCoverage() {
        return coverage;
    }

    public void setCoverage(final List<CoverageView> coverage) {
        this.coverage = coverage;
    }

}
