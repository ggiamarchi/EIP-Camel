package com.zenika.camel.notifier.processing;

import org.springframework.stereotype.Component;

import com.zenika.camel.notifier.model.Admin;

@Component("notificationTransformer")
public class NotificationTransformer {

	public String transform(Admin admin) {
		return new StringBuilder()
		.append(admin.getSeverity())
		.append(";")	
		.append(admin.getMessage())
		.toString();
	}

}
