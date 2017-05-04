package io.pivotal.scs.demo.etl.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import io.pivotal.scs.demo.etl.jdbc.processor.JdbcEventProcessor;
import io.pivotal.scs.demo.etl.jdbc.processor.JdbcEventProcessorConfiguration;
import io.pivotal.scs.demo.etl.jdbc.source.JdbcEventSource;
import io.pivotal.scs.demo.etl.jdbc.source.JdbcEventSourceConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(
	classes={
		ScsEtlIntegrationTests.JdbcEventProcessorApplication.class,
		JdbcEventSourceConfiguration.class,
		JdbcEventProcessorConfiguration.class
	},
	properties ="spring.datasource.url=jdbc:h2:mem:test",
	webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@DirtiesContext
public abstract class ScsEtlIntegrationTests {

	@Autowired
	protected JdbcEventSource source;
	
	@Autowired
	protected JdbcEventProcessor processor;

	@Autowired
	protected MessageCollector messageCollector;

	@TestPropertySource(properties = {
			"jdbc-event-source.query=select src_key from db_events "
					+ "where src_group = 'CUSTOMER' and status_code = '0' group by src_key",
			"jdbc-event-source.update=update db_events set status_code='1' "
					+ "where src_group = 'CUSTOMER' and src_key in (:src_key)",
			"jdbc-event-source.maxRowsPerPoll=2" })
	public static class CustomerEventTests extends ScsEtlIntegrationTests {

		@Test
		public void testExtraction() throws InterruptedException {
			for (int i = 0; i < 3; i++) {
				Message<?> received = messageCollector.forChannel(source.output()).poll(1, TimeUnit.SECONDS);
				assertNotNull(received);
				assertThat(received.getPayload(), Matchers.instanceOf(Map.class));
				assertNotNull(((Map) received.getPayload()).get("SRC_KEY"));
				processor.input().send(received);
				received = messageCollector.forChannel(processor.output()).poll(1, TimeUnit.SECONDS);
				assertNotNull(received);
				assertThat(received.getPayload(), Matchers.instanceOf(Map.class));
				assertNotNull(((Map) received.getPayload()).get("SRC_KEY"));
			}
			Message<?> received = messageCollector.forChannel(source.output()).poll(1, TimeUnit.SECONDS);
			assertNull(received);
		}

	}

	@TestPropertySource(properties = {
			"jdbc-event-source.query=select src_key from db_events "
					+ "where src_group = 'ORDER' and status_code = '0' group by src_key",
			"jdbc-event-source.update=update db_events set status_code='1' "
					+ "where src_group = 'ORDER' and src_key in (:src_key)",
			"jdbc-event-source.maxRowsPerPoll=2" })
	public static class OrderEventTests extends ScsEtlIntegrationTests {

		@Test
		public void testExtraction() throws InterruptedException {
			for (int i = 0; i < 3; i++) {
				Message<?> received = messageCollector.forChannel(source.output()).poll(1, TimeUnit.SECONDS);
				assertNotNull(received);
				assertThat(received.getPayload(), Matchers.instanceOf(Map.class));
				assertNotNull(((Map) received.getPayload()).get("SRC_KEY"));
				processor.input().send(received);
				received = messageCollector.forChannel(processor.output()).poll(1, TimeUnit.SECONDS);
				assertNotNull(received);
				assertThat(received.getPayload(), Matchers.instanceOf(Map.class));
				assertNotNull(((Map) received.getPayload()).get("SRC_KEY"));
			}
			Message<?> received = messageCollector.forChannel(source.output()).poll(1, TimeUnit.SECONDS);
			assertNull(received);
		}

	}

	@TestPropertySource(properties = {
			"jdbc-event-source.query=select src_key from db_events "
					+ "where src_group = 'ITEM' and status_code = '0' group by src_key",
			"jdbc-event-source.update=update db_events set status_code='1' "
					+ "where src_group = 'ITEM' and src_key in (:src_key)",
			"jdbc-event-source.maxRowsPerPoll=2" })
	public static class ItemEventTests extends ScsEtlIntegrationTests {

		@Test
		public void testExtraction() throws InterruptedException {
			for (int i = 0; i < 2; i++) {
				Message<?> received = messageCollector.forChannel(source.output()).poll(1, TimeUnit.SECONDS);
				assertNotNull(received);
				assertThat(received.getPayload(), Matchers.instanceOf(Map.class));
				assertNotNull(((Map) received.getPayload()).get("SRC_KEY"));
				processor.input().send(received);
				received = messageCollector.forChannel(processor.output()).poll(1, TimeUnit.SECONDS);
				assertNotNull(received);
				assertThat(received.getPayload(), Matchers.instanceOf(Map.class));
				assertNotNull(((Map) received.getPayload()).get("SRC_KEY"));
			}
			Message<?> received = messageCollector.forChannel(source.output()).poll(1, TimeUnit.SECONDS);
			assertNull(received);
		}

	}

	@SpringBootApplication
	public static class JdbcEventProcessorApplication {

	}

}