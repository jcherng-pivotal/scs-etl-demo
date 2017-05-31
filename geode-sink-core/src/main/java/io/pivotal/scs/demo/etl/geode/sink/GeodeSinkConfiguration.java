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

package io.pivotal.scs.demo.etl.geode.sink;

import org.apache.geode.cache.client.ClientCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.AggregatorSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.support.Consumer;

import io.pivotal.scs.demo.etl.geode.sink.messaging.GeodeMessageAggregator;
import io.pivotal.scs.demo.etl.geode.sink.messaging.GeodeMessageHandler;

/**
 * @author Jeff Cherng
 */
@EnableBinding(GeodeSink.class)
@EnableConfigurationProperties(GeodeSinkProperties.class)
public class GeodeSinkConfiguration {
	
	@Autowired
	private ApplicationContext context;

	@Autowired
	private GeodeSink sink;

	@Autowired
	private GeodeSinkProperties properties;
	
	@Autowired
	private GeodeMessageAggregator geodeAggregator;
	
	@Autowired
	private Consumer<AggregatorSpec> geodeAggregateConsumer;
	
	@Bean
	GeodeMessageAggregator geodeAggregator() {
		return new GeodeMessageAggregator(context, properties.getAggregatorGroupCount(), properties.getBatchSize());
	}
	
	@Bean
	Consumer<AggregatorSpec> geodeAggregateConsumer() {
		return new Consumer<AggregatorSpec>() {
			@Override
			public void accept(AggregatorSpec aggregatorSpec) {
				aggregatorSpec.processor(geodeAggregator, null);
				aggregatorSpec.groupTimeout(properties.getBatchTimeout());
				aggregatorSpec.sendPartialResultOnExpiry(true);
				aggregatorSpec.expireGroupsUponCompletion(true);
				aggregatorSpec.expireGroupsUponTimeout(true);
				// TODO: persisted message store
				// aggregatorSpec.messageStore(messageStore)
			}
		};
	}

	@Bean
	IntegrationFlow sinkFlow(ClientCache clientCache) {
		return IntegrationFlows.from(this.sink.input())
				.aggregate(geodeAggregateConsumer)
				.handle(new GeodeMessageHandler(clientCache))
				.get();
	}
}
