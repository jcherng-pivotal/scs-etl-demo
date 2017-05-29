package io.pivotal.scs.demo.etl.geode.sink.messaging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.integration.annotation.Aggregator;
import org.springframework.integration.annotation.CorrelationStrategy;
import org.springframework.integration.annotation.ReleaseStrategy;
import org.springframework.messaging.Message;

import io.pivotal.scs.demo.model.etl.PayloadWrapper;

public class GeodeMessageAggregator {

	private final int groupCount;
	private final int batchSize;

	public GeodeMessageAggregator(int groupCount, int batchSize) {
		super();
		this.groupCount = groupCount;
		this.batchSize = batchSize;
	}

	@Aggregator
	public Map<?, ?> output(List<Message<?>> messages) {
		Map<Object, Object> finalPayload = new HashMap<>();
		for (Message<?> message : messages) {
			finalPayload.put(message.getHeaders().get("geodeKey"), message.getPayload());
		}
		return finalPayload;
	}

	@CorrelationStrategy
	public String correlation(Message<PayloadWrapper<?>> message) {
		int hashCode = message.getHeaders().get("geodeKey").hashCode();
		int divisor = groupCount;
		return "[" + (String) message.getHeaders().get("geodeRegionName") + "]" 
				+ ",[" + (String) message.getHeaders().get("geodeActionChannel") + "]" 
				+ ",[" + Math.floorMod(hashCode, divisor) + "]";
	}

	@ReleaseStrategy
	public boolean canMessagesBeReleased(List<Message<PayloadWrapper<?>>> messages) {
		return messages.size() >= batchSize;
	}

}
