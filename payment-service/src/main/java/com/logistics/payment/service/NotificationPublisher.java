package com.logistics.payment.service;

import com.logistics.payment.dto.event.NotificationEvent;

public interface NotificationPublisher {

	void publish(NotificationEvent event);

}