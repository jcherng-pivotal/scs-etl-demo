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
	private Integer aggregatorGroupCount = 10;
	private Integer batchSize = 1000;
	private Integer batchTimeout = 1000;

	public Integer getAggregatorGroupCount() {
		return aggregatorGroupCount;
	}

	public void setAggregatorGroupCount(Integer aggregatorGroupCount) {
		this.aggregatorGroupCount = aggregatorGroupCount;
	}

	public Integer getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(Integer batchSize) {
		this.batchSize = batchSize;
	}

	public Integer getBatchTimeout() {
		return batchTimeout;
	}

	public void setBatchTimeout(Integer batchTimeout) {
		this.batchTimeout = batchTimeout;
	}

}
