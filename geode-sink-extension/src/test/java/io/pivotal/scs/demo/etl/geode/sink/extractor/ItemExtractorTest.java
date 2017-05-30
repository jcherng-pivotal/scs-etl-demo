package io.pivotal.scs.demo.etl.geode.sink.extractor;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import io.pivotal.scs.demo.model.etl.ItemPayload;
import io.pivotal.scs.demo.model.etl.PayloadWrapper;
import io.pivotal.scs.demo.model.geode.pdx.Item;

public class ItemExtractorTest {

	@Test
	public void testExtractData() {
		ItemExtractor extractor = new ItemExtractor();
		PayloadWrapper<ItemPayload> payloadWrapper = new PayloadWrapper<ItemPayload>(null);
		assertThat(extractor.extractData(payloadWrapper)).isNull();
		
		ItemPayload payload = new ItemPayload();
		payload.setId("id");
		payload.setName("name");
		payload.setDescription("description");
		payload.setPrice(new BigDecimal("9.99"));
		payloadWrapper = new PayloadWrapper<ItemPayload>(payload);
		
		Item expected = new Item();
		expected.setName("name");
		expected.setDescription("description");
		expected.setPrice("9.99");
		assertThat(extractor.extractData(payloadWrapper)).isEqualTo(expected);
	}

}
