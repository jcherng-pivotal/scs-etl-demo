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
