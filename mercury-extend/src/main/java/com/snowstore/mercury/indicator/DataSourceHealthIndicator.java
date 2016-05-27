/*
 * Copyright 2012-2015 the original author or authors.
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

package com.snowstore.mercury.indicator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.snowstore.mercury.core.health.Health;
import com.snowstore.mercury.core.indicator.AbstractHealthIndicator;
import com.snowstore.mercury.core.indicator.HealthIndicator;

/**
 * {@link HealthIndicator} that tests the status of a {@link DataSource} and
 * optionally runs a test query.
 * 
 * @author sm
 */
public class DataSourceHealthIndicator extends AbstractHealthIndicator implements InitializingBean {

	private static final Map<String, String> PRODUCT_SPECIFIC_QUERIES;
	static {
		Map<String, String> queries = new HashMap<String, String>();
		queries.put("HSQL Database Engine", "SELECT COUNT(*) FROM " + "INFORMATION_SCHEMA.SYSTEM_USERS");
		queries.put("Oracle", "SELECT 'Hello' from DUAL");
		queries.put("Apache Derby", "SELECT 1 FROM SYSIBM.SYSDUMMY1");
		PRODUCT_SPECIFIC_QUERIES = Collections.unmodifiableMap(queries);
	}

	private static final String DEFAULT_QUERY = "SELECT 1";

	@Autowired(required = false)
	private DataSource dataSource;

	private String query;

	@Autowired(required = false)
	private JdbcTemplate jdbcTemplate;

	/**
	 * Create a new {@link DataSourceHealthIndicator} instance.
	 */
	public DataSourceHealthIndicator() {

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			Assert.state(this.dataSource != null, "DataSource for DataSourceHealthIndicator must be specified");
			this.jdbcTemplate = new JdbcTemplate(dataSource);

		} catch (Exception e) {
			valid = false;
		}
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		if (valid) {
			if (this.dataSource == null) {
				builder.up().withDetail("database", "unknown");
			} else {
				doDataSourceHealthCheck(builder);
				addBuilder(builder);
			}
		} else {
			builder.none();
		}
	}

	private void doDataSourceHealthCheck(Health.Builder builder) throws Exception {
		String[] product = getProduct().split(";");

		builder.up().withDetail("database", product[0]);
		String validationQuery = getValidationQuery(product[0]);
		if (StringUtils.hasText(validationQuery)) {
			try {
				// Avoid calling getObject as it breaks MySQL on Java 7
				List<Object> results = this.jdbcTemplate.query(validationQuery, new SingleColumnRowMapper());
				Object result = DataAccessUtils.requiredSingleResult(results);
				builder.withDetail("hello", result);
				builder.withDetail(SERVER, product[1]);
			} catch (Exception ex) {
				builder.down(ex);
			}
		}
	}

	private String getProduct() {
		return this.jdbcTemplate.execute(new ConnectionCallback<String>() {
			@Override
			public String doInConnection(Connection connection) throws SQLException, DataAccessException {
				return connection.getMetaData().getDatabaseProductName() + ";" + connection.getMetaData().getURL();
			}
		});
	}

	protected String getValidationQuery(String product) {
		String query = this.query;
		if (!StringUtils.hasText(query)) {
			query = PRODUCT_SPECIFIC_QUERIES.get(product);
		}
		if (!StringUtils.hasText(query)) {
			query = DEFAULT_QUERY;
		}
		return query;
	}

	/**
	 * Set the {@link DataSource} to use.
	 * 
	 * @param dataSource
	 *            the data source
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Set a specific validation query to use to validate a connection. If none
	 * is set, a default validation query is used.
	 * 
	 * @param query
	 *            the query
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * Return the validation query or {@code null}.
	 * 
	 * @return the query
	 */
	public String getQuery() {
		return this.query;
	}

	/**
	 * {@link RowMapper} that expects and returns results from a single column.
	 */
	private static class SingleColumnRowMapper implements RowMapper<Object> {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			ResultSetMetaData metaData = rs.getMetaData();
			int columns = metaData.getColumnCount();
			if (columns != 1) {
				throw new IncorrectResultSetColumnCountException(1, columns);
			}
			return JdbcUtils.getResultSetValue(rs, 1);
		}

	}

}
