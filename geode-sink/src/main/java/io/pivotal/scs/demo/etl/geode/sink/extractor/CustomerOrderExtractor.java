package io.pivotal.scs.demo.etl.geode.sink.extractor;

import io.pivotal.scs.demo.model.etl.CustomerOrderPayload;
import io.pivotal.scs.demo.model.etl.PayloadWrapper;
import io.pivotal.scs.demo.model.geode.pdx.CustomerOrder;

public class CustomerOrderExtractor implements PayloadWrapperExtractor<CustomerOrderPayload, CustomerOrder> {

	@Override
	public CustomerOrder extractData(PayloadWrapper<CustomerOrderPayload> payloadWrapper) {
		CustomerOrder value = new CustomerOrder();
		if (payloadWrapper.hasPayload()) {
			CustomerOrderPayload payload = payloadWrapper.getPayload();
			value.setCustomerId(payload.getCustomerId());
			value.setShippingAddress(payload.getShippingAddress());
			value.setOrderDate(payload.getOrderDate());
			value.setItemSet(payload.getItemSet());
		}
		return value;
	}

}
