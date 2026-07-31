package com.logistics.notification.service;

import com.logistics.notification.dto.NotificationEvent;

public interface NotificationService {

	void sendNotification(NotificationEvent event);

}