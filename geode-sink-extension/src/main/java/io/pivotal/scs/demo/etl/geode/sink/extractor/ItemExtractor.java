package io.pivotal.scs.demo.etl.geode.sink.extractor;

import io.pivotal.scs.demo.model.etl.ItemPayload;
import io.pivotal.scs.demo.model.etl.PayloadWrapper;
import io.pivotal.scs.demo.model.geode.pdx.Item;

public class ItemExtractor implements PayloadWrapperExtractor<ItemPayload, Item> {

	@Override
	public Item extractData(PayloadWrapper<ItemPayload> payloadWrapper) {
		Item value = null;
		if (payloadWrapper.hasPayload()) {
			value = new Item();
			ItemPayload payload = payloadWrapper.getPayload();
			value.setName(payload.getName());
			value.setDescription(payload.getDescription());
			value.setPrice(payload.getPrice().toString());
		}
		return value;
	}

}
