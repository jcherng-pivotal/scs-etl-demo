package io.pivotal.scs.demo.etl.geode.sink.messaging;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

public class GeodeMessageHandler {
	public void handleMessage(Message<GeodeDataWrapper> message) {
		MessageHeaders headers = message.getHeaders();
		GeodeDataWrapper payload = message.getPayload();
		String regionName = (String) headers.get("srcGroup");
		Region<Object, Object> region = ClientCacheFactory.getAnyInstance().getRegion(regionName);
		region.putAll(payload.getMap());
		region.removeAll(payload.getSet());
	}
}
