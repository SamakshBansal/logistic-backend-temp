package com.logistics.notification.service.impl;

import org.springframework.stereotype.Service;

import com.logistics.notification.dto.NotificationEvent;
import com.logistics.notification.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Override
	public void sendNotification(NotificationEvent event) {

		System.out.println("=========================================");
		System.out.println("Notification Received");
		System.out.println("Event Type : " + event.getEventType());
		System.out.println("Order ID   : " + event.getOrderId());
		System.out.println("Customer ID: " + event.getCustomerId());
		System.out.println("Message    : " + event.getMessage());
		System.out.println("Time       : " + event.getTimestamp());
		System.out.println("=========================================");

	}
}