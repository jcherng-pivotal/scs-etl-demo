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

package io.pivotal.scs.demo.etl.geode.sink;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Holds configuration properties for the Geode Sink module.
 *
 * @author Jeff Cherng
 */
@ConfigurationProperties("geode-sink")
@Validated
public class GeodeSinkProperties {
	private String locator;
	private Integer port;
	private Integer aggregatorGroupCount = 10;
	private Integer putBatchSize = 1000;
	private Integer putBatchTimeout = 1000;
	private Integer removeBatchSize = 2000;
	private Integer removeBatchTimeout = 2000;

	public String getLocator() {
		return locator;
	}

	public void setLocator(String locator) {
		this.locator = locator;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getAggregatorGroupCount() {
		return aggregatorGroupCount;
	}

	public void setAggregatorGroupCount(Integer aggregatorGroupCount) {
		this.aggregatorGroupCount = aggregatorGroupCount;
	}

	public Integer getPutBatchSize() {
		return putBatchSize;
	}

	public void setPutBatchSize(Integer putBatchSize) {
		this.putBatchSize = putBatchSize;
	}

	public Integer getPutBatchTimeout() {
		return putBatchTimeout;
	}

	public void setPutBatchTimeout(Integer putBatchTimeout) {
		this.putBatchTimeout = putBatchTimeout;
	}

	public Integer getRemoveBatchSize() {
		return removeBatchSize;
	}

	public void setRemoveBatchSize(Integer removeBatchSize) {
		this.removeBatchSize = removeBatchSize;
	}

	public Integer getRemoveBatchTimeout() {
		return removeBatchTimeout;
	}

	public void setRemoveBatchTimeout(Integer removeBatchTimeout) {
		this.removeBatchTimeout = removeBatchTimeout;
	}

}
