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

package io.pivotal.scs.demo.etl.jdbc.processor;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.PooledServiceConnectorConfig.PoolConfig;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("cloud")
public class CloudDataSourceConfig extends AbstractCloudConfig {

	private static final Logger LOG = LoggerFactory.getLogger(CloudDataSourceConfig.class);

	@Value("${spring.datasource.tomcat.min-idle:10} ")
	private int minIdle;
	@Value("${spring.datasource.tomcat.max-idle:100}")
	private int maxIdle;
	@Value("${spring.datasource.tomcat.max-wait:3000}")
	private int maxWait;

	@Bean
	public DataSource dataSource() {
		LOG.info("[CloudDataSourceConfig] minIdle: " + minIdle + " maxIdle: " + maxIdle + " maxWait: " + maxWait);
		PoolConfig poolConfig = new PoolConfig(minIdle, maxIdle, maxWait);
		DataSourceConfig dbConfig = new DataSourceConfig(poolConfig, null);
		return connectionFactory().dataSource(dbConfig);
	}

}
