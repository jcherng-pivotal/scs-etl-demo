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

import org.junit.Test;

import io.pivotal.scs.demo.model.etl.CustomerPayload;
import io.pivotal.scs.demo.model.etl.PayloadWrapper;
import io.pivotal.scs.demo.model.geode.pdx.Customer;

public class CustomerExtractorTest {

	@Test
	public void testExtractData() {
		CustomerExtractor extractor = new CustomerExtractor();
		PayloadWrapper<CustomerPayload> payloadWrapper = new PayloadWrapper<CustomerPayload>(null);
		assertThat(extractor.extractData(payloadWrapper)).isNull();
		
		CustomerPayload payload = new CustomerPayload();
		payload.setId("id");
		payload.setName("name");
		payloadWrapper = new PayloadWrapper<CustomerPayload>(payload);
		
		Customer expected = new Customer();
		expected.setName("name");
		assertThat(extractor.extractData(payloadWrapper)).isEqualTo(expected);
	}

}
