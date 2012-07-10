package com.zenika.camel.notifier;
import java.util.ResourceBundle;

public class Config {

	private Config() {}

	private static final String BUNDLE_NAME = "META-INF.spring.config";
	private static final ResourceBundle CONFIG = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static String get(String key) {
		return CONFIG.getString(key);
	}

}
