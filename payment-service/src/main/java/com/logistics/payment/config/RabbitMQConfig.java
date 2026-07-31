package com.logistics.payment.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String EXCHANGE = "logistics.exchange";

	public static final String ROUTING_KEY = "notification.routingKey";

	@Bean
	DirectExchange exchange() {
		return new DirectExchange(EXCHANGE);
	}

	@Bean
	JacksonJsonMessageConverter messageConverter() {
		return new JacksonJsonMessageConverter();
	}

}