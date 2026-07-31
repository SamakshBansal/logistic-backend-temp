package com.logistics.order.service.impl;

import com.logistics.order.enums.PackageType;
import com.logistics.order.service.PricingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingServiceImpl implements PricingService {

	@Override
	public BigDecimal calculatePrice(PackageType packageType, Double weight) {

		double baseCharge;
		double ratePerKg;

		switch (packageType) {

		case DOCUMENT -> {
			baseCharge = 50;
			ratePerKg = 20;
		}

		case PARCEL -> {
			baseCharge = 100;
			ratePerKg = 30;
		}

		case ELECTRONICS -> {
			baseCharge = 200;
			ratePerKg = 50;
		}

		case CLOTHING -> {
			baseCharge = 80;
			ratePerKg = 25;
		}

		case FOOD -> {
			baseCharge = 120;
			ratePerKg = 35;
		}

		default -> {
			baseCharge = 100;
			ratePerKg = 30;
		}
		}

		return BigDecimal.valueOf(baseCharge + (weight * ratePerKg));
	}
}