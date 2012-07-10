package com.zenika.camel.notifier.processing;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import com.zenika.camel.notifier.model.Notifications;
import com.zenika.camel.notifier.model.ObjectFactory;
import com.zenika.camel.notifier.model.User;

public class Aggregator implements AggregationStrategy {

	private ObjectFactory objectFactory;

	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange){
		Message inNew = newExchange.getIn();
		User user = inNew.getBody(User.class);
		if (oldExchange == null) {
			// First time
			Notifications notifications = objectFactory.createNotifications();
			notifications.getUser().add(user);
			inNew.setBody(notifications);
			return newExchange;
		}
		Message inOld = oldExchange.getIn();
		Notifications notifications = (Notifications) inOld.getBody();
		notifications.getUser().add(user);
		return oldExchange;
	}

}
