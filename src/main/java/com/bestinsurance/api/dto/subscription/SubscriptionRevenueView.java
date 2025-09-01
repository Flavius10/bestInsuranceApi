package com.bestinsurance.api.dto.subscription;

import java.math.BigDecimal;

public record SubscriptionRevenueView(String name, BigDecimal revenue, Long customerCount) {
}
