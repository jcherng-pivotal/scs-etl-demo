package io.pivotal.scs.demo.etl.geode.sink.messaging;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.integration.annotation.CorrelationStrategy;
import org.springframework.integration.annotation.ReleaseStrategy;
import org.springframework.messaging.Message;

import io.pivotal.scs.demo.etl.geode.sink.extractor.PayloadWrapperExtractor;
import io.pivotal.scs.demo.model.etl.PayloadWrapper;

public class GeodeMessageAggregator {

	private final ApplicationContext context;
	private final int groupCount;
	private final int batchSize;

	public GeodeMessageAggregator(ApplicationContext context, int groupCount, int batchSize) {
		super();
		this.context = context;
		this.groupCount = groupCount;
		this.batchSize = batchSize;
	}

	@Aggregator
	public GeodeDataWrapper output(List<Message<PayloadWrapper<?>>> messages) {
		GeodeDataWrapper geodeDataWrapper = new GeodeDataWrapper();
		for (Message<PayloadWrapper<?>> message : messages) {
			String geodeRegionName = (String) message.getHeaders().get("srcGroup");
			Object geodeKey = message.getHeaders().get("srcKey");
			PayloadWrapper payloadWrapper = message.getPayload();
			PayloadWrapperExtractor<?, ?> extractor = 
					(PayloadWrapperExtractor<?, ?>) context.getBean(geodeRegionName + "Extractor");
			if(payloadWrapper.hasPayload()){
				geodeDataWrapper.getSet().remove(geodeKey);
				geodeDataWrapper.getMap().put(geodeKey, extractor.extractData(payloadWrapper));
			} else {
				geodeDataWrapper.getMap().remove(geodeKey);
				geodeDataWrapper.getSet().add(geodeKey);
			}
		}
		return geodeDataWrapper;
	}

	@CorrelationStrategy
	public String correlation(Message<PayloadWrapper<?>> message) {
		int hashCode = message.getHeaders().get("srcKey").hashCode();
		int divisor = groupCount;
		return "[" + (String) message.getHeaders().get("srcGroup") + "]" 
				+ ",[" + Math.floorMod(hashCode, divisor) + "]";
	}

	@ReleaseStrategy
	public boolean canMessagesBeReleased(List<Message<PayloadWrapper<?>>> messages) {
		return messages.size() >= batchSize;
	}

}
