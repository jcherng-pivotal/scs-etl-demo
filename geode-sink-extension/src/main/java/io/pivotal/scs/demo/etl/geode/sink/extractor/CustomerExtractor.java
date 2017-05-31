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

import io.pivotal.scs.demo.model.etl.CustomerPayload;
import io.pivotal.scs.demo.model.etl.PayloadWrapper;
import io.pivotal.scs.demo.model.geode.pdx.Customer;

public class CustomerExtractor implements PayloadWrapperExtractor<CustomerPayload, Customer> {

	@Override
	public Customer extractData(PayloadWrapper<CustomerPayload> payloadWrapper) {
		Customer value = null;
		if (payloadWrapper.hasPayload()) {
			value = new Customer();
			CustomerPayload payload = payloadWrapper.getPayload();
			value.setName(payload.getName());
		}
		return value;
	}

}
