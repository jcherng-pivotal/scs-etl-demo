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

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.springframework.dao.DataAccessException;

import io.pivotal.scs.demo.model.etl.CustomerOrderPayload;
import io.pivotal.scs.demo.model.etl.PayloadWrapper;

/**
 * @author Jeff Cherng
 */
public class CustomerOrderResultSetExtractorTest {

	@Test
	public void testExtractData() throws DataAccessException, SQLException {
		Date orderDate = new Date(12345L);
		List<String> items = Arrays.asList("item1", "item2");
		
		CustomerOrderResultSetExtractor extractor = new CustomerOrderResultSetExtractor();
		ResultSet rs = mock(ResultSet.class);
		given(rs.next()).willReturn(true).willReturn(true).willReturn(false);
		given(rs.getString("ID")).willReturn("order1");
		given(rs.getString("CUSTOMER_ID")).willReturn("customer1");
		given(rs.getString("S_ADDRESS")).willReturn("address");
		given(rs.getDate("ORDER_TS")).willReturn(orderDate);
		given(rs.getString("ITEM_ID")).willReturn("item1").willReturn("item2");

		CustomerOrderPayload payload = new CustomerOrderPayload();
		payload.setId("order1");
		payload.setCustomerId("customer1");
		payload.setOrderDate(orderDate.getTime());
		payload.setShippingAddress("address");
		payload.setItemSet(new HashSet<>(items));
		
		PayloadWrapper<CustomerOrderPayload> payloadWrapper = new PayloadWrapper<CustomerOrderPayload>(payload);
		assertThat(extractor.extractData(rs)).isEqualTo(payloadWrapper);
	}

}
