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

package io.pivotal.scs.demo.etl.geode.sink.extractor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import io.pivotal.scs.demo.model.etl.CustomerOrderPayload;
import io.pivotal.scs.demo.model.etl.PayloadWrapper;
import io.pivotal.scs.demo.model.geode.pdx.CustomerOrder;

public class CustomerOrderExtractorTest {

	@Test
	public void testExtractData() {
		CustomerOrderExtractor extractor = new CustomerOrderExtractor();
		PayloadWrapper<CustomerOrderPayload> payloadWrapper = new PayloadWrapper<CustomerOrderPayload>(null);
		assertThat(extractor.extractData(payloadWrapper)).isNull();
		
		Date orderDate = new Date();
		List<String> items = Arrays.asList("item1", "item2");
		CustomerOrderPayload payload = new CustomerOrderPayload();
		payload.setId("id");
		payload.setCustomerId("customerId");
		payload.setOrderDate(orderDate.getTime());
		payload.setShippingAddress("shippingAddress");
		payload.setItemSet(new HashSet<>(items));
		payloadWrapper = new PayloadWrapper<CustomerOrderPayload>(payload);
		
		CustomerOrder expected = new CustomerOrder();
		expected.setCustomerId("customerId");
		expected.setOrderDate(orderDate.getTime());
		expected.setShippingAddress("shippingAddress");
		expected.setItemSet(new HashSet<>(items));
		assertThat(extractor.extractData(payloadWrapper)).isEqualTo(expected);
	}

}
