/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.pivotal.scs.demo.etl.jdbc.processor.messaging;

import java.util.Date;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import io.pivotal.scs.demo.etl.jdbc.processor.dao.PayloadWrapperDao;
import io.pivotal.scs.demo.model.etl.PayloadWrapper;

public class JdbcEventMessageHandler {
	private final ApplicationContext context;

	public JdbcEventMessageHandler(ApplicationContext context) {
		super();
		this.context = context;
	}

	public Message<?> handleMessage(Message<Map<String, ?>> message) {
		MessageHeaders headers = message.getHeaders();
		Map<String, ?> requestPayload = message.getPayload();
		String srcGroup = (String) requestPayload.get("SRC_GROUP");
		String srcKey = (String) requestPayload.get("SRC_KEY");
		String actionCode = (String) requestPayload.get("ACTION_CODE");
		long originalMessageTimestamp = (long) headers.get(MessageHeaders.TIMESTAMP);
		long originalEventTimestamp = (long) ((Date) requestPayload.get("EVENT_TS")).getTime();
		PayloadWrapperDao<?> dao = (PayloadWrapperDao<?>) context.getBean(srcGroup + "Dao");
		PayloadWrapper<?> payloadWrapper = dao.getData(srcKey);
		Message<?> processedMessage = MessageBuilder
				.withPayload(payloadWrapper)
				.copyHeaders(headers)
				.setHeader("srcGroup", srcGroup)
				.setHeader("srcKey", srcKey)
				.setHeader("actionCode", actionCode)
				.setHeader("originalMessageTimestamp", originalMessageTimestamp)
				.setHeader("originalEventTimestamp", originalEventTimestamp)
				.build();
		return processedMessage;
	}

}
