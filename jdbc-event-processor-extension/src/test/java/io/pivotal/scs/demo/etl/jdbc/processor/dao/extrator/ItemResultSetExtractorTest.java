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

package io.pivotal.scs.demo.etl.jdbc.processor.dao.extrator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import io.pivotal.scs.demo.model.etl.ItemPayload;
import io.pivotal.scs.demo.model.etl.PayloadWrapper;

/**
 * @author Jeff Cherng
 */
public class ItemResultSetExtractorTest {

	@Test
	public void testExtractData() throws SQLException {
		ItemResultSetExtractor extractor = new ItemResultSetExtractor();
		ResultSet rs = mock(ResultSet.class);
		given(rs.next()).willReturn(true).willReturn(false);
		given(rs.getString("ID")).willReturn("item1");
		given(rs.getString("NAME")).willReturn("item name");
		given(rs.getString("DESCRIPTION")).willReturn("description");
		given(rs.getBigDecimal("PRICE")).willReturn(new BigDecimal("1.99"));

		ItemPayload payload = new ItemPayload();
		payload.setId("item1");
		payload.setName("item name");
		payload.setDescription("description");
		payload.setPrice(new BigDecimal("1.99"));
		PayloadWrapper<ItemPayload> payloadWrapper = new PayloadWrapper<ItemPayload>(payload);
		assertThat(extractor.extractData(rs)).isEqualTo(payloadWrapper);
	}

}
