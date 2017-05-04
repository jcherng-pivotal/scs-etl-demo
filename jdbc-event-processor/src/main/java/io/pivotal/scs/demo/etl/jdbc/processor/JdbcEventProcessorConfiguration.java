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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;

/**
 * A module that reads data from input and RDBMS by using JDBC to creates a payload.
 *
 * @author Jeff Cherng
 */
@EnableBinding(JdbcEventProcessor.class)
@EnableConfigurationProperties(JdbcEventProcessorProperties.class)
public class JdbcEventProcessorConfiguration {

	@Autowired
	private JdbcEventProcessorProperties properties;

	@Transformer(inputChannel = JdbcEventProcessor.INPUT, outputChannel = JdbcEventProcessor.OUTPUT)
	public Message<?> transform(Message<?> message) {
		return message;
	}
	
}
