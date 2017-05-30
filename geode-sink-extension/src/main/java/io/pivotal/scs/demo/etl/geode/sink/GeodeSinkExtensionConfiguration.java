package io.pivotal.scs.demo.etl.geode.sink;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.pivotal.scs.demo.etl.geode.sink.extractor.CustomerExtractor;
import io.pivotal.scs.demo.etl.geode.sink.extractor.CustomerOrderExtractor;
import io.pivotal.scs.demo.etl.geode.sink.extractor.ItemExtractor;
import io.pivotal.scs.demo.etl.geode.sink.extractor.PayloadWrapperExtractor;
import io.pivotal.scs.demo.model.etl.CustomerOrderPayload;
import io.pivotal.scs.demo.model.etl.CustomerPayload;
import io.pivotal.scs.demo.model.etl.ItemPayload;
import io.pivotal.scs.demo.model.geode.pdx.Customer;
import io.pivotal.scs.demo.model.geode.pdx.CustomerOrder;
import io.pivotal.scs.demo.model.geode.pdx.Item;

/**
 * @author Jeff Cherng
 */
@Configuration
public class GeodeSinkExtensionConfiguration {
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
}
