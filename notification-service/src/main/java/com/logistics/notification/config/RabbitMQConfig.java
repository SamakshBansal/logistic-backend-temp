package com.logistics.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String EXCHANGE = "logistics.exchange";

	public static final String NOTIFICATION_QUEUE = "notification.queue";

	public static final String ROUTING_KEY = "notification.routingKey";

	@Bean
	Queue notificationQueue() {
		return new Queue(NOTIFICATION_QUEUE);
	}

	@Bean
	DirectExchange exchange() {
		return new DirectExchange(EXCHANGE);
	}

	@Bean
	Binding binding() {
		return BindingBuilder.bind(notificationQueue()).to(exchange()).with(ROUTING_KEY);
	}

	@Bean
	ApplicationRunner rabbitRunner() {
		return args -> System.out.println("RabbitMQConfig Loaded");
	}

	@Bean
	JacksonJsonMessageConverter messageConverter() {
		return new JacksonJsonMessageConverter();
	}
}