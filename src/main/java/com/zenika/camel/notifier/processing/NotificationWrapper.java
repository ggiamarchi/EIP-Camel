package com.zenika.camel.notifier.processing;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import com.zenika.camel.notifier.model.Admin;
import com.zenika.camel.notifier.model.Notifications;
import com.zenika.camel.notifier.model.ObjectFactory;

public class NotificationWrapper implements Processor {

	private ObjectFactory objectFactory;

	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		Admin admin = message.getBody(Admin.class);
		Notifications notifications = objectFactory.createNotifications();
		notifications.getAdmin().add(admin);
		message.setBody(notifications);
	}
	
}
