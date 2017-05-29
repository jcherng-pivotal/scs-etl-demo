package io.pivotal.scs.demo.etl.geode.sink.messaging;

import java.util.Map;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

public class GeodeMessageHandler {
	public void handleMessage(Message<Map<?, ?>> message) {
		MessageHeaders headers = message.getHeaders();
		Map<?, ?> payload = message.getPayload();
		String regionName = (String) headers.get("geodeRegionName");
		Region region = ClientCacheFactory.getAnyInstance().getRegion(regionName);
		if ("geodePutChannel".equals(headers.get("geodeActionChannel"))) {
			region.putAll(payload);
		} else if ("geodeRemoveChannel".equals(headers.get("geodeActionChannel"))) {
			region.removeAll(payload.keySet());
		}
	}
}
