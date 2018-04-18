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

import java.net.URI;

import javax.annotation.PreDestroy;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.PdxSerializer;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.pivotal.spring.cloud.service.common.GemfireServiceInfo;

/**
 * @author Jeff Cherng
 */
@Configuration
public class GeodeClientConfiguration {

	private static final String SECURITY_CLIENT = "security-client-auth-init";
	private static final String SECURITY_USERNAME = "security-username";
	private static final String SECURITY_PASSWORD = "security-password";

	@Autowired
	private ClientCache clientCache;

	@Bean
	PdxSerializer pdxSerializer() {
		return new ReflectionBasedAutoSerializer("io.pivotal.scs.demo.model.geode.pdx.*");
	}

	@Bean
	Region customerRegion(ClientRegionFactory clientRegionFactory) {
		return clientRegionFactory.create("customer");
	}

	@Bean
	Region customerOrderRegion(ClientRegionFactory clientRegionFactory) {
		return clientRegionFactory.create("customerOrder");
	}

	@Bean
	Region itemRegion(ClientRegionFactory clientRegionFactory) {
		return clientRegionFactory.create("item");
	}

	@PreDestroy
	public void cleanUp() throws Exception {
		clientCache.close();
	}

	@Profile("default")
	protected static class DefaultConfiguration {
		@Bean
		@ConditionalOnMissingBean({ Cache.class, ClientCache.class })
		@ConditionalOnClass({ ClientCache.class })
		ClientCache clientCache(PdxSerializer pdxSerializer) {
			return new ClientCacheFactory().setPdxSerializer(pdxSerializer).create();
		}

		@Bean
		ClientRegionFactory clientRegionFactory(ClientCache clientCache) {
			return clientCache.createClientRegionFactory(ClientRegionShortcut.LOCAL);
		}
	}

	@Profile("cloud")
	protected static class CloudConfiguration {
		@Autowired
		private GeodeSinkProperties properties;

		@Bean
		@ConditionalOnMissingBean({ Cache.class, ClientCache.class })
		@ConditionalOnClass({ ClientCache.class })
		ClientCache clientCache(PdxSerializer pdxSerializer) {
			Cloud cloud = new CloudFactory().getCloud();
			for (ServiceInfo serviceInfo : cloud.getServiceInfos()) {
				if (serviceInfo instanceof GemfireServiceInfo) {
					GemfireServiceInfo gemfireServiceInfo = (GemfireServiceInfo) serviceInfo;
					ClientCacheFactory factory = new ClientCacheFactory();
					for (URI locator : gemfireServiceInfo.getLocators()) {
						factory.addPoolLocator(locator.getHost(), locator.getPort());
					}

					factory.set(SECURITY_CLIENT, "io.pivotal.scs.demo.etl.geode.sink.UserAuthInitialize.create");
					factory.set(SECURITY_USERNAME, gemfireServiceInfo.getUsername());
					factory.set(SECURITY_PASSWORD, gemfireServiceInfo.getPassword());
					factory.setPdxSerializer(pdxSerializer);
					factory.setPoolSubscriptionEnabled(true); // to enable CQ
					return factory.create();
				}
			}

			throw new IllegalArgumentException("Gemfire Service Not Found!!");
		}

		@Bean
		ClientRegionFactory clientRegionFactory(ClientCache clientCache) {
			return clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY);
		}
	}
}
