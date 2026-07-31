package com.logistics.payment.service.impl;

import com.logistics.payment.config.RabbitMQConfig;
import com.logistics.payment.dto.event.NotificationEvent;
import com.logistics.payment.service.NotificationPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationPublisherImpl implements NotificationPublisher {

	private final RabbitTemplate rabbitTemplate;

	@Override
	public void publish(NotificationEvent event) {

		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, event);

		System.out.println("Notification Event Published");

	}

}