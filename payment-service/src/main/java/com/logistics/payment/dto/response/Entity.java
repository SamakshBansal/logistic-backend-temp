package com.logistics.payment.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Entity {

	private String id;

	private String order_id;

	private String status;

	private String method;

	private Integer amount;
}
