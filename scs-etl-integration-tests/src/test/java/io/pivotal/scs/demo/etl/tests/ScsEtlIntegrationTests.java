package io.pivotal.scs.demo.etl.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.geode.cache.Region;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import io.pivotal.scs.demo.etl.geode.sink.GeodeClientConfiguration;
import io.pivotal.scs.demo.etl.geode.sink.GeodeSink;
import io.pivotal.scs.demo.etl.geode.sink.GeodeSinkConfiguration;
import io.pivotal.scs.demo.etl.jdbc.processor.JdbcEventProcessor;
import io.pivotal.scs.demo.etl.jdbc.processor.JdbcEventProcessorConfiguration;
import io.pivotal.scs.demo.etl.jdbc.source.JdbcEventSource;
import io.pivotal.scs.demo.etl.jdbc.source.JdbcEventSourceConfiguration;
import io.pivotal.scs.demo.model.etl.CustomerOrderPayload;
import io.pivotal.scs.demo.model.etl.CustomerPayload;
import io.pivotal.scs.demo.model.etl.ItemPayload;
import io.pivotal.scs.demo.model.etl.PayloadWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest(
	classes={
		ScsEtlIntegrationTests.TestApplication.class,
		JdbcEventSourceConfiguration.class,
		JdbcEventProcessorConfiguration.class,
		GeodeSinkConfiguration.class,
		GeodeClientConfiguration.class
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
	protected GeodeSink sink;

	@Autowired
	protected MessageCollector messageCollector;
	
	@Autowired
	protected Region customerRegion;
	
	@Autowired
	protected Region customerOrderRegion;
	
	@Autowired
	protected Region itemRegion;

	@TestPropertySource(properties = {
			"jdbc-event-source.query=select src_group, src_key, action_code, max(event_ts) as event_ts from db_events "
					+ "where src_group = 'customer' "
					+ "and action_code = 'U' "
					+ "and status_code = '0' "
					+ "group by src_group, src_key, action_code",
			"jdbc-event-source.update=update db_events set status_code='1' "
					+ "where src_group = 'customer' and src_key in (:src_key)",
			"jdbc-event-source.maxRowsPerPoll=10" })
	public static class CustomerEventTests extends ScsEtlIntegrationTests {

		@Test
		public void testExtraction() throws InterruptedException {
			for (int i = 0; i < 2; i++) {
				Message<?> received = messageCollector.forChannel(source.output()).poll(2, TimeUnit.SECONDS);
				assertNotNull(received);
				assertThat(received.getPayload(), Matchers.instanceOf(Map.class));
				assertNotNull(((Map) received.getPayload()).get("SRC_KEY"));
				processor.input().send(received);
				received = messageCollector.forChannel(processor.output()).poll(2, TimeUnit.SECONDS);
				assertNotNull(received);
				assertThat(received.getPayload(), Matchers.instanceOf(PayloadWrapper.class));
				PayloadWrapper payloadWrapper = (PayloadWrapper) received.getPayload();
				assertThat(payloadWrapper.getPayload(), Matchers.instanceOf(CustomerPayload.class));
				sink.input().send(received);
			}
			Message<?> received = messageCollector.forChannel(source.output()).poll(2, TimeUnit.SECONDS);
			assertNull(received);
			assertThat(customerRegion.keySet().size(), Matchers.is(2));
		}

	}

	@TestPropertySource(properties = {
			"jdbc-event-source.query=select src_group, src_key, action_code, max(event_ts) as event_ts from db_events "
					+ "where src_group = 'customerOrder' "
					+ "and action_code = 'U' "
					+ "and status_code = '0' "
					+ "group by src_group, src_key, action_code",
			"jdbc-event-source.update=update db_events set status_code='1' "
					+ "where src_group = 'customerOrder' and src_key in (:src_key)",
			"jdbc-event-source.maxRowsPerPoll=10" })
	public static class CustomerOrderEventTests extends ScsEtlIntegrationTests {

		@Test
		public void testExtraction() throws InterruptedException {
			for (int i = 0; i < 3; i++) {
				Message<?> received = messageCollector.forChannel(source.output()).poll(2, TimeUnit.SECONDS);
				assertNotNull(received);
				assertThat(received.getPayload(), Matchers.instanceOf(Map.class));
				assertNotNull(((Map) received.getPayload()).get("SRC_KEY"));
				processor.input().send(received);
				received = messageCollector.forChannel(processor.output()).poll(2, TimeUnit.SECONDS);
				assertNotNull(received);
				assertThat(received.getPayload(), Matchers.instanceOf(PayloadWrapper.class));
				PayloadWrapper payloadWrapper = (PayloadWrapper) received.getPayload();
				assertThat(payloadWrapper.getPayload(), Matchers.instanceOf(CustomerOrderPayload.class));
				sink.input().send(received);
			}
			Message<?> received = messageCollector.forChannel(source.output()).poll(2, TimeUnit.SECONDS);
			assertNull(received);
			assertThat(customerOrderRegion.keySet().size(), Matchers.is(3));
		}

	}

	@TestPropertySource(properties = {
			"jdbc-event-source.query=select src_group, src_key, action_code, max(event_ts) as event_ts from db_events "
					+ "where src_group = 'item' "
					+ "and action_code = 'U' "
					+ "and status_code = '0' "
					+ "group by src_group, src_key, action_code",
			"jdbc-event-source.update=update db_events set status_code='1' "
					+ "where src_group = 'item' and src_key in (:src_key)",
			"jdbc-event-source.maxRowsPerPoll=10" })
	public static class ItemEventTests extends ScsEtlIntegrationTests {

		@Test
		public void testExtraction() throws InterruptedException {
			for (int i = 0; i < 3; i++) {
				Message<?> received = messageCollector.forChannel(source.output()).poll(2, TimeUnit.SECONDS);
				assertNotNull(received);
				assertThat(received.getPayload(), Matchers.instanceOf(Map.class));
				assertNotNull(((Map) received.getPayload()).get("SRC_KEY"));
				processor.input().send(received);
				received = messageCollector.forChannel(processor.output()).poll(2, TimeUnit.SECONDS);
				assertNotNull(received);
				assertThat(received.getPayload(), Matchers.instanceOf(PayloadWrapper.class));
				PayloadWrapper payloadWrapper = (PayloadWrapper) received.getPayload();
				assertThat(payloadWrapper.getPayload(), Matchers.instanceOf(ItemPayload.class));
				sink.input().send(received);
			}
			Message<?> received = messageCollector.forChannel(source.output()).poll(2, TimeUnit.SECONDS);
			assertNull(received);
			assertThat(itemRegion.keySet().size(), Matchers.is(3));
		}

	}
	
	@Sql({"/delta-data.sql"})
	@TestPropertySource(properties = {
			"jdbc-event-source.query=select (src_group, src_key) as groupKey, "
					+ "src_group, src_key, action_code, max(event_ts) as event_ts from db_events "
					+ "where status_code = '0' "
					+ "group by src_group, src_key, action_code",
			"jdbc-event-source.update=update db_events set status_code='1' "
					+ "where (src_group, src_key) in (:groupKey)",
			"jdbc-event-source.maxRowsPerPoll=10" })
	public static class AllEventTests extends ScsEtlIntegrationTests {

		@Test
		public void testExtraction() throws InterruptedException {
			boolean hasMoreData = true;
			do {
				Message<?> received = messageCollector.forChannel(source.output()).poll(2, TimeUnit.SECONDS);
				if (received != null) {
					processor.input().send(received);
					received = messageCollector.forChannel(processor.output()).poll(2, TimeUnit.SECONDS);
					sink.input().send(received);
				} else {
					hasMoreData = false;
				}
			} while (hasMoreData);
			assertThat(customerRegion.keySet().size(), Matchers.is(2));
			assertThat(customerOrderRegion.keySet().size(), Matchers.is(2));
			assertThat(itemRegion.keySet().size(), Matchers.is(3));
		}

	}

	@SpringBootApplication
	public static class TestApplication {

	}

}