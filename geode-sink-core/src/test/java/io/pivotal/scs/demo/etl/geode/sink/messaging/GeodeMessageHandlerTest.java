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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;
import java.util.Set;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

public class GeodeMessageHandlerTest {

	private GeodeMessageHandler geodeMessageHandler;
	private ClientCache clientCache;
	private Region<?,?> region;


	@Before
	@SuppressWarnings("unchecked")
	public void setUp() throws Exception {
		clientCache = mock(ClientCache.class);
		region = mock(Region.class);
		given(clientCache.getRegion("foo")).willReturn((Region<Object, Object>) region);

		geodeMessageHandler = new GeodeMessageHandler(clientCache);
	}


	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testHandleMessage() {
		GeodeDataWrapper geodeDataWrapper = new GeodeDataWrapper();
		geodeDataWrapper.getMap().put("key1", "value1");
		geodeDataWrapper.getMap().put("key2", "value2");
		geodeDataWrapper.getSet().add("key1");

		Message<GeodeDataWrapper> message = MessageBuilder
		.withPayload(geodeDataWrapper)
		.setHeader("srcGroup", "foo")
		.build();
		
		geodeMessageHandler.handleMessage(message);

		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);
		verify(region, times(1)).putAll(mapCaptor.capture());
		assertThat(geodeDataWrapper.getMap(), is(mapCaptor.getValue()));

		ArgumentCaptor<Set> setCaptor = ArgumentCaptor.forClass(Set.class);
		verify(region, times(1)).removeAll(setCaptor.capture());
		assertThat(geodeDataWrapper.getMap(), is(mapCaptor.getValue()));
	}

}
