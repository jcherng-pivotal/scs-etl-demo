package io.pivotal.scs.demo.etl.geode.sink.extractor;

import io.pivotal.scs.demo.model.etl.CustomerPayload;
import io.pivotal.scs.demo.model.etl.PayloadWrapper;
import io.pivotal.scs.demo.model.geode.pdx.Customer;

public class CustomerExtractor implements PayloadWrapperExtractor<CustomerPayload, Customer> {

	@Override
	public Customer extractData(PayloadWrapper<CustomerPayload> payloadWrapper) {
		Customer value = new Customer();
		if (payloadWrapper.hasPayload()) {
			CustomerPayload payload = payloadWrapper.getPayload();
			value.setName(payload.getName());
		}
		return value;
	}

}
