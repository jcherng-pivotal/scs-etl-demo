/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
