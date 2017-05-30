package io.pivotal.scs.demo.etl.geode.sink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.AggregatorSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.support.Consumer;

import io.pivotal.scs.demo.etl.geode.sink.extractor.CustomerExtractor;
import io.pivotal.scs.demo.etl.geode.sink.extractor.CustomerOrderExtractor;
import io.pivotal.scs.demo.etl.geode.sink.extractor.ItemExtractor;
import io.pivotal.scs.demo.etl.geode.sink.extractor.PayloadWrapperExtractor;
import io.pivotal.scs.demo.etl.geode.sink.messaging.GeodeMessageAggregator;
import io.pivotal.scs.demo.etl.geode.sink.messaging.GeodeMessageHandler;
import io.pivotal.scs.demo.model.etl.CustomerOrderPayload;
import io.pivotal.scs.demo.model.etl.CustomerPayload;
import io.pivotal.scs.demo.model.etl.ItemPayload;
import io.pivotal.scs.demo.model.geode.pdx.Customer;
import io.pivotal.scs.demo.model.geode.pdx.CustomerOrder;
import io.pivotal.scs.demo.model.geode.pdx.Item;

/**
 * @author Jeff Cherng
 */
@EnableBinding(GeodeSink.class)
@EnableConfigurationProperties(GeodeSinkProperties.class)
public class GeodeSinkConfiguration {
	
	@Autowired
	private ApplicationContext context;

	@Autowired
	private GeodeSink sink;

	@Autowired
	private GeodeSinkProperties properties;
	
	@Autowired
	private GeodeMessageAggregator geodeAggregator;
	
	@Autowired
	private Consumer<AggregatorSpec> geodeAggregateConsumer;

	@Bean
	PayloadWrapperExtractor<CustomerPayload, Customer> customerExtractor() {
		return new CustomerExtractor();
	}

	@Bean
	PayloadWrapperExtractor<CustomerOrderPayload, CustomerOrder> customerOrderExtractor() {
		return new CustomerOrderExtractor();
	}

	@Bean
	PayloadWrapperExtractor<ItemPayload, Item> itemExtractor() {
		return new ItemExtractor();
	}
	
	@Bean
	GeodeMessageAggregator geodeAggregator() {
		return new GeodeMessageAggregator(context, properties.getAggregatorGroupCount(), properties.getBatchSize());
	}
	
	@Bean
	Consumer<AggregatorSpec> geodeAggregateConsumer() {
		return new Consumer<AggregatorSpec>() {
			@Override
			public void accept(AggregatorSpec aggregatorSpec) {
				aggregatorSpec.processor(geodeAggregator, null);
				aggregatorSpec.groupTimeout(properties.getBatchTimeout());
				aggregatorSpec.sendPartialResultOnExpiry(true);
				aggregatorSpec.expireGroupsUponCompletion(true);
				aggregatorSpec.expireGroupsUponTimeout(true);
			}
		};
	}

	@Bean
	IntegrationFlow sinkFlow() {
		return IntegrationFlows.from(this.sink.input())
				.aggregate(geodeAggregateConsumer)
				.handle(new GeodeMessageHandler())
				.get();
	}
}
