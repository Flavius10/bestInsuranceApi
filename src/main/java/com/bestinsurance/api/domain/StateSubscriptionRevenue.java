package com.bestinsurance.api.domain;

import java.math.BigDecimal;

public record StateSubscriptionRevenue(String name, BigDecimal revenue, Long customerCount) {
}
