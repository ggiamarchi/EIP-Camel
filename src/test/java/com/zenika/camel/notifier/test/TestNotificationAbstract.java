package com.zenika.camel.notifier.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;

import com.zenika.camel.notifier.model.Notifications;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "/META-INF/spring/broker-embedded.xml",
	"/META-INF/spring/commons.xml"
})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class TestNotificationAbstract {

	private static final String XML_ENCODING = "UTF-8";
	private static final boolean XML_PRETTY = true;

	@Value("${jaxb.model.pkg}")
	private String JAXB_MODEL_PKG;

	@Autowired
	private ActiveMQConnectionFactory jmsConnectionFactory;

	private static final String QUEUE_IN = "notifications.in"; 
	private static final String QUEUE_OUT_ADMIN = "notifications.admin.out";
	private static final String QUEUE_OUT_USERS = "notifications.user.out";

	@Test
	public void testNotificationSystem() {
		try {
			sendFileAsTextMessage("data/in/notifications01.xml", QUEUE_IN);
			sendFileAsTextMessage("data/in/notifications02.xml", QUEUE_IN);
			sendFileAsTextMessage("data/in/notifications03.xml", QUEUE_IN);
			sendFileAsTextMessage("data/in/notifications04.xml", QUEUE_IN);

			String admin = consumeTextMessage(QUEUE_OUT_ADMIN);
			String adminRef = readFile("data/out/admin.csv");

			Assert.assertEquals(adminRef, admin);

			Notifications alice, bob, n1, n2;
			n1 = consumeNotificationMessage(QUEUE_OUT_USERS);
			n2 = consumeNotificationMessage(QUEUE_OUT_USERS);

			alice = readFileAsNotification("data/out/alice.xml");
			bob = readFileAsNotification("data/out/bob.xml");
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
		return unmarshall(consumeTextMessage(queueName), XML_ENCODING);
	}

	protected final String consumeTextMessage(String queueName) throws JAXBException, IOException, JMSException {
		Connection connection = jmsConnectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		ActiveMQQueue queue = new ActiveMQQueue(queueName);
		MessageConsumer consumer = session.createConsumer(queue);
		connection.start();
		TextMessage message = (TextMessage) consumer.receive();
		session.close();
		connection.close();
		return message.getText();
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
		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filePath)));
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
