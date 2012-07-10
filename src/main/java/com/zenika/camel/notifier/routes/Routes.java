package com.zenika.camel.notifier.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;

import com.zenika.camel.notifier.Config;
import com.zenika.camel.notifier.model.Admin;


public class Routes extends RouteBuilder {

	public static final Namespaces NS = new Namespaces("n", Config.get("jaxb.model.namespace"));

	// Endpoints URI
	public static final String ENDPOINT_URI_IN = "jms:notifications.in";
	public static final String ENDPOINT_URI_ROUTER = "direct:router";
	public static final String ENDPOINT_URI_USER_AGGREGATOR = "direct:userAggregator";
	public static final String ENDPOINT_URI_ADMIN_FILTER = "direct:adminFilter";
	public static final String ENDPOINT_URI_ADMIN_TRANSLATOR = "direct:adminTranslator";
	public static final String ENDPOINT_URI_USER_OUT = "jms:notifications.user.out";
	public static final String ENDPOINT_URI_ADMIN_OUT = "jms:notifications.admin.out";
	public static final String ENDPOINT_URI_UNKNOWN_NOTIFICATION = "jms:notifications.unknown.out";

	@Override
	public void configure() throws Exception {

		// Splitter
		from(ENDPOINT_URI_IN)
			.to(log("Input messages"))
			.split()
				.xpath("/n:notifications/*", NS)
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
				.to(log("BLABLA"))
				.to(ENDPOINT_URI_USER_OUT);


		// Filter
		from(ENDPOINT_URI_ADMIN_FILTER)
			.to(log("Admin message filter"))
			.filter()
				.xpath("/n:admin/n:severity/text() = 'ERROR'", NS)
				.to(ENDPOINT_URI_ADMIN_TRANSLATOR);


		// Admin messages translator
		from(ENDPOINT_URI_ADMIN_TRANSLATOR)
			.to(log("Admin message translator"))
			.convertBodyTo(Admin.class)
			.processRef("wrapper")
			.convertBodyTo(String.class)
			.to(ENDPOINT_URI_ADMIN_OUT);

	}
	
	private String log(String message) {
		return "log:[Java DSL - " + message + "]?showAll=true&multiline=true";
	}

}
