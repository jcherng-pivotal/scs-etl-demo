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

package io.pivotal.scs.demo.etl.geode.sink.messaging;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import io.pivotal.scs.demo.etl.geode.sink.extractor.PayloadWrapperExtractor;
import io.pivotal.scs.demo.model.etl.PayloadWrapper;

public class GeodeMessageAggregatorTest {
	private final int groupCount = 2;
	private final int batchSize = 2;

	private ApplicationContext context;
	private GeodeMessageAggregator geodeMessageAggregator;
	private PayloadWrapperExtractor<?, ?> extractor;

	@Before
	public void setUp() throws Exception {
		context = mock(ApplicationContext.class);
		extractor = mock(PayloadWrapperExtractor.class);
		given(context.getBean("fooExtractor")).willReturn(extractor);
		geodeMessageAggregator = new GeodeMessageAggregator(context, groupCount, batchSize);
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testOutput() {
		List<Message<PayloadWrapper>> messages = new ArrayList<>();
		Message<PayloadWrapper> message1 = MessageBuilder
		.withPayload(new PayloadWrapper(new Object()))
		.setHeader("srcGroup", "foo")
		.setHeader("srcKey", "key1")
		.build();
		messages.add(message1);
		
		Message<PayloadWrapper> message2 = MessageBuilder
		.withPayload(new PayloadWrapper(new Object()))
		.setHeader("srcGroup", "foo")
		.setHeader("srcKey", "key2")
		.build();
		messages.add(message2);
		
		Message<PayloadWrapper> message3 = MessageBuilder
		.withPayload(new PayloadWrapper(null))
		.setHeader("srcGroup", "foo")
		.setHeader("srcKey", "key1")
		.build();
		messages.add(message3);
		
		GeodeDataWrapper geodeDataWrapper =  geodeMessageAggregator.output(messages);
		
		assertThat(geodeDataWrapper.getMap().containsKey("key1"), is(false));
		assertThat(geodeDataWrapper.getMap().containsKey("key2"), is(true));
	}

	@Test
	public void testCorrelation() {
		Message<PayloadWrapper<Object>> message = MessageBuilder
		.withPayload(new PayloadWrapper<Object>(new Object()))
		.setHeader("srcGroup", "foo")
		.setHeader("srcKey", "key1")
		.build();
		
		String expectedResult = "[foo],[" 
		+ Math.floorMod("key1".hashCode(), groupCount) + "]";
		
		assertThat(geodeMessageAggregator.correlation(message), is(expectedResult));
	}

	@Test
	public void testCanMessagesBeReleased() {
		List<Message<?>> messages = new ArrayList<>();
		Message<PayloadWrapper<Object>> message1 = MessageBuilder
		.withPayload(new PayloadWrapper<Object>(null))
		.setHeader("srcGroup", "foo")
		.setHeader("srcKey", "key1")
		.build();
		messages.add(message1);
		assertThat(geodeMessageAggregator.canMessagesBeReleased(messages), is(false));
		
		Message<PayloadWrapper<Object>> message2 = MessageBuilder
		.withPayload(new PayloadWrapper<>(new Object()))
		.setHeader("srcGroup", "foo")
		.setHeader("srcKey", "key2")
		.build();
		messages.add(message2);
		assertThat(geodeMessageAggregator.canMessagesBeReleased(messages), is(true));
	}

}
