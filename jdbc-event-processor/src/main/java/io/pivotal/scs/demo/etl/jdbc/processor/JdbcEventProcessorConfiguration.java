/*
 * Copyright 2016 the original author or authors.
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import io.pivotal.scs.demo.etl.jdbc.processor.dao.PayloadWrapperDao;
import io.pivotal.scs.demo.etl.jdbc.processor.dao.extrator.CustomerOrderResultSetExtractor;
import io.pivotal.scs.demo.etl.jdbc.processor.dao.extrator.CustomerResultSetExtractor;
import io.pivotal.scs.demo.etl.jdbc.processor.dao.extrator.ItemResultSetExtractor;
import io.pivotal.scs.demo.etl.jdbc.processor.messaging.JdbcEventMessageHandler;
import io.pivotal.scs.demo.model.etl.CustomerOrderPayload;
import io.pivotal.scs.demo.model.etl.CustomerPayload;
import io.pivotal.scs.demo.model.etl.ItemPayload;

/**
 * A module that reads data from input and RDBMS by using JDBC to creates a
 * payload.
 *
 * @author Jeff Cherng
 */
@EnableBinding(JdbcEventProcessor.class)
@EnableConfigurationProperties(JdbcEventProcessorProperties.class)
@PropertySource({ "classpath:src-group-sqls.xml" })
public class JdbcEventProcessorConfiguration {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private JdbcEventProcessorProperties properties;

	@Autowired
	private JdbcEventProcessor processor;

	@Autowired
	private JdbcEventMessageHandler jdbcEventMessageHandler;

	@Autowired
	private DataSource dataSource;

	@Bean
	PayloadWrapperDao<CustomerPayload> customerDao() {
		return new PayloadWrapperDao<>(dataSource, new CustomerResultSetExtractor(), properties.getCustomerSql());
	}

	@Bean
	PayloadWrapperDao<ItemPayload> itemDao() {
		return new PayloadWrapperDao<>(dataSource, new ItemResultSetExtractor(), properties.getItemSql());
	}

	@Bean
	PayloadWrapperDao<CustomerOrderPayload> customerOrderDao() {
		return new PayloadWrapperDao<>(dataSource, new CustomerOrderResultSetExtractor(),
				properties.getCustomerOrderSql());
	}

	@Bean
	JdbcEventMessageHandler jdbcEventMessageHandler() {
		return new JdbcEventMessageHandler(context);
	}

	@Bean
	IntegrationFlow processorFlow() {
		return IntegrationFlows
				.from(this.processor.input())
				.handle(jdbcEventMessageHandler)
				.channel(this.processor.output())
				.get();
	}

}
