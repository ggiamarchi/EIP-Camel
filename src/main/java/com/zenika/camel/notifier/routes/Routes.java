package com.zenika.camel.notifier.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("notificationRoutes")
public class Routes extends RouteBuilder {

	@Value("${jaxb.model.namespace}")
	private String JAXB_MODEL_NAMESPACE;
	
	@Value("${endpoint.in}")
	private String ENDPOINT_URI_IN;

	@Value("${endpoint.router}")
	private String ENDPOINT_URI_ROUTER;
	
	@Value("${endpoint.user.aggregator}")
	private String ENDPOINT_URI_USER_AGGREGATOR;
	
	@Value("${endpoint.admin.filter}")
	private String ENDPOINT_URI_ADMIN_FILTER;
	
	@Value("${endpoint.admin.translator}")
	private String ENDPOINT_URI_ADMIN_TRANSLATOR;
	
	@Value("${endpoint.user.out}")
	private String ENDPOINT_URI_USER_OUT;
	
	@Value("${endpoint.admin.out}")
	private String ENDPOINT_URI_ADMIN_OUT;
	
	@Value("${endpoint.unknown.notification}")
	private String ENDPOINT_URI_UNKNOWN_NOTIFICATION;

	@Override
	public void configure() throws Exception {

		Namespaces NS = new Namespaces("n", JAXB_MODEL_NAMESPACE);

		// Splitter
		from(ENDPOINT_URI_IN)
			.to(log("Input messages"))
			.split().xpath("/n:notifications/*", NS)
			.to(ENDPOINT_URI_ROUTER);


		// Content based router
		from(ENDPOINT_URI_ROUTER)
			.to(log("Router"))
			.choice()
				.when().xpath("/n:user", NS)
					.to(ENDPOINT_URI_USER_AGGREGATOR)
				.when().xpath("/n:admin", NS)
					.to(ENDPOINT_URI_ADMIN_FILTER)
				.otherwise()
					.to(ENDPOINT_URI_UNKNOWN_NOTIFICATION);


		// User messages Aggregator
		from(ENDPOINT_URI_USER_AGGREGATOR)
			.to(log("User notification aggregator"))
			.aggregate()
				.xpath("/n:user/n:to", NS)
				.aggregationStrategyRef("aggregator")
				.completionSize(10)
				.completionTimeout(5000)
			.convertBodyTo(String.class)
			.to(ENDPOINT_URI_USER_OUT);


		// Filter
		from(ENDPOINT_URI_ADMIN_FILTER)
			.to(log("Admin message filter"))
			.filter().xpath("/n:admin/n:severity/text() = 'ERROR'", NS)
			.to(ENDPOINT_URI_ADMIN_TRANSLATOR);


		// Admin messages translator
		from(ENDPOINT_URI_ADMIN_TRANSLATOR)
			.to(log("Admin message translator"))
			.beanRef("notificationTransformer")
			.to(ENDPOINT_URI_ADMIN_OUT);
		
	}
	
	private String log(String message) {
		return "log:[Java DSL - " + message + "]?showAll=true&multiline=true";
	}

}
