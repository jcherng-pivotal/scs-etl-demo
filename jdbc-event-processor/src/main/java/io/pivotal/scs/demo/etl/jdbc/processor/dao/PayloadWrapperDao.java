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

package io.pivotal.scs.demo.etl.jdbc.processor.dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import io.pivotal.scs.demo.model.etl.PayloadWrapper;

/**
 * Generic Dao to retrieve object base on the given sql and srcKey
 *
 * @author Jeff Cherng
 */
public class PayloadWrapperDao<T> {

	private final NamedParameterJdbcOperations jdbcOperations;
	private final ResultSetExtractor<PayloadWrapper<T>> messageBuilderExtractor;
	private final String sql;

	public PayloadWrapperDao(DataSource dataSource, ResultSetExtractor<PayloadWrapper<T>> resultSetExtractor, String sql) {
		this.jdbcOperations = new NamedParameterJdbcTemplate(dataSource);
		this.messageBuilderExtractor = resultSetExtractor;
		this.sql = sql;
	}

	public PayloadWrapper<T> getData(String srcKey) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("srcKey", srcKey);
		return this.jdbcOperations.query(sql, paramMap, messageBuilderExtractor);
	}

}
