package com.logistics.order.service;

import com.logistics.order.enums.PackageType;

import java.math.BigDecimal;

public interface PricingService {

	BigDecimal calculatePrice(PackageType packageType, Double weight);

}