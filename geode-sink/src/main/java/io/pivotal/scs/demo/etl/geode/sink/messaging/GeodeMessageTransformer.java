package io.pivotal.scs.demo.etl.geode.sink.messaging;

import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import io.pivotal.scs.demo.etl.geode.sink.extractor.PayloadWrapperExtractor;
import io.pivotal.scs.demo.model.etl.PayloadWrapper;

public class GeodeMessageTransformer {
	private final ApplicationContext context;

	public GeodeMessageTransformer(ApplicationContext context) {
		super();
		this.context = context;
	}

	public Message<?> transform(Message<PayloadWrapper<?>> message) {
		MessageHeaders headers = message.getHeaders();
		PayloadWrapper payloadWrapper = message.getPayload();
		String geodeActionChannel = (payloadWrapper.hasPayload()) ?
				"geodePutChannel" : "geodeRemoveChannel";
		String geodeRegionName = (String) headers.get("srcGroup");
		String geodeKey = (String) headers.get("srcKey");
		PayloadWrapperExtractor<?, ?> extractor = (PayloadWrapperExtractor<?, ?>) context.getBean(geodeRegionName + "Extractor");

		return MessageBuilder
				.withPayload(extractor.extractData(payloadWrapper))
				.copyHeaders(message.getHeaders())
				.setHeader("geodeActionChannel", geodeActionChannel)
				.setHeader("geodeRegionName", geodeRegionName)
				.setHeader("geodeKey", geodeKey)
				.build();
	}
}
