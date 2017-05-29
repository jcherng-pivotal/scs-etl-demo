package io.pivotal.scs.demo.etl.geode.sink;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class GeodeClientConfiguration {

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
			return new ClientCacheFactory()
					.setPdxSerializer(pdxSerializer)
					.create();
		}
		
		@Bean
		ClientRegionFactory clientRegionFactory(ClientCache clientCache) {
			return clientCache.createClientRegionFactory(ClientRegionShortcut.LOCAL);
		}
	}

	@Profile("cloud")
	protected static class CloudConfiguration {
		@Bean
		@ConditionalOnMissingBean({ Cache.class, ClientCache.class })
		@ConditionalOnClass({ ClientCache.class })
		ClientCache clientCache(PdxSerializer pdxSerializer) {
			// TODO: access the locator from binding service
			return new ClientCacheFactory()
					.setPdxSerializer(pdxSerializer)
					.addPoolLocator("127.0.0.1", 10334)
					.create();
		}
		
		@Bean
		ClientRegionFactory clientRegionFactory(ClientCache clientCache) {
			return clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY);
		}
	}
}
