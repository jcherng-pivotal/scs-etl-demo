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

package io.pivotal.scs.demo.etl.geode.sink.extractor;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import io.pivotal.scs.demo.model.etl.ItemPayload;
import io.pivotal.scs.demo.model.etl.PayloadWrapper;
import io.pivotal.scs.demo.model.geode.pdx.Item;

/**
 * @author Jeff Cherng
 */
public class ItemExtractorTest {

	@Test
	public void testExtractData() {
		ItemExtractor extractor = new ItemExtractor();
		PayloadWrapper<ItemPayload> payloadWrapper = new PayloadWrapper<ItemPayload>(null);
		assertThat(extractor.extractData(payloadWrapper)).isNull();
		
		ItemPayload payload = new ItemPayload();
		payload.setId("id");
		payload.setName("name");
		payload.setDescription("description");
		payload.setPrice(new BigDecimal("9.99"));
		payloadWrapper = new PayloadWrapper<ItemPayload>(payload);
		
		Item expected = new Item();
		expected.setName("name");
		expected.setDescription("description");
		expected.setPrice("9.99");
		assertThat(extractor.extractData(payloadWrapper)).isEqualTo(expected);
	}

}
