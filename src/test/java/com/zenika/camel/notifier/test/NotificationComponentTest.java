package com.zenika.camel.notifier.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.camel.CamelContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zenika.camel.notifier.Config;
import com.zenika.camel.notifier.model.Notifications;


public class NotificationComponentTest {

	private static final String SPRING_CONF = "META-INF/spring/commons.xml";
	private static final String BROKER_CONF = "META-INF/spring/broker-embedded.xml";
	private static final String CAMEL_CONF  = "META-INF/spring/camel-context-java.xml";

	private static final String XML_ENCODING = "UTF-8";
	private static final boolean XML_PRETTY = true;
	private static final String JAXB_MODEL_PKG = Config.get("jaxb.model.pkg");

	private static final String QUEUE_IN = "notifications.in"; 
	private static final String QUEUE_OUT_ADMIN = "notifications.admin.out";
	private static final String QUEUE_OUT_USERS = "notifications.user.out";

	private ClassPathXmlApplicationContext context;
	private CamelContext camelContext;
	private ActiveMQConnectionFactory jmsConnectionFactory;

	@Before
	public void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext(SPRING_CONF, BROKER_CONF, CAMEL_CONF);
		jmsConnectionFactory = context.getBean(ActiveMQConnectionFactory.class);
		camelContext = context.getBean(CamelContext.class);
	}
	
	@After
	public void tearDown() throws Exception {
		camelContext.stop();
	}

	@Test
	public void testNotificationSystem() {
		try {
			sendFileAsTextMessage("data-test/in/notifications01.xml", QUEUE_IN);
			sendFileAsTextMessage("data-test/in/notifications02.xml", QUEUE_IN);
			sendFileAsTextMessage("data-test/in/notifications03.xml", QUEUE_IN);
			sendFileAsTextMessage("data-test/in/notifications04.xml", QUEUE_IN);

			Notifications admin, adminRef;
			admin = consumeNotificationMessage(QUEUE_OUT_ADMIN);
			adminRef = readFileAsNotification("data-test/out/admin.xml");
			Assert.assertEquals(admin, adminRef);

			Notifications alice, bob, n1, n2;
			n1 = consumeNotificationMessage(QUEUE_OUT_USERS);
			n2 = consumeNotificationMessage(QUEUE_OUT_USERS);

			alice = readFileAsNotification("data-test/out/alice.xml");
			bob = readFileAsNotification("data-test/out/bob.xml");
			Assert.assertTrue((n1.equals(alice) && n2.equals(bob) || n2.equals(alice) && n1.equals(bob))); // Don't know the messages arrival order
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected final void sendFileAsTextMessage(String filepath, String queueName) throws JMSException, JAXBException, IOException {
		sendTextMessage(readFile(filepath), queueName, XML_PRETTY, XML_ENCODING);
	}

	protected final Notifications consumeNotificationMessage(String queueName) throws JAXBException, IOException, JMSException {
		Connection connection = jmsConnectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		ActiveMQQueue queue = new ActiveMQQueue(queueName);
		MessageConsumer consumer = session.createConsumer(queue);
		connection.start();
		TextMessage message = (TextMessage) consumer.receive();
		session.close();
		connection.close();
		return unmarshall(message.getText(), XML_ENCODING);
	}

	protected final Notifications readFileAsNotification(String filePath) throws JAXBException, IOException {
		return unmarshall(readFile(filePath), XML_ENCODING);
	}

	protected final void sendTextMessage(String text, String queueName, boolean prettyPrint, String encoding) throws JMSException, JAXBException, IOException {
		Connection connection = jmsConnectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = new ActiveMQQueue(queueName);
		MessageProducer messageProducer = session.createProducer(destination);
		Message message = session.createTextMessage(text);
		messageProducer.send(message);
		messageProducer.close();
		session.close();
		connection.close();
	}

	private Notifications unmarshall(String document, String encoding) throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(JAXB_MODEL_PKG);
		Unmarshaller um = context.createUnmarshaller();
		return (Notifications) um.unmarshal(new ByteArrayInputStream(document.getBytes(encoding)));
	}

	private String readFile(String filePath) throws IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

}
