package com.logistics.notification.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.logistics.notification.config.RabbitMQConfig;
import com.logistics.notification.dto.NotificationEvent;
import com.logistics.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

	private final NotificationService notificationService;

	@RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
	public void consume(NotificationEvent event) {
		
		System.out.println("===== MESSAGE RECEIVED =====");
		
		notificationService.sendNotification(event);

	}
}