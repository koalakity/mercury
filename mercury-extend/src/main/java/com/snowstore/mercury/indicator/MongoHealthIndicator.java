/*
 * Copyright 2012-2014 the original author or authors.
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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.Assert;

import com.mongodb.CommandResult;
import com.snowstore.mercury.core.health.Health;
import com.snowstore.mercury.core.indicator.AbstractHealthIndicator;
import com.snowstore.mercury.core.indicator.HealthIndicator;

/**
 * Simple implementation of a {@link HealthIndicator} returning status
 * information for Mongo data stores.
 * 
 * @author sm
 */
public class MongoHealthIndicator extends AbstractHealthIndicator implements InitializingBean {

	@Autowired(required = false)
	private MongoTemplate mongoTemplate;

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		if (valid) {
			CommandResult result = this.mongoTemplate.executeCommand("{ buildInfo: 1 }");
			builder.up().withDetail("version", result.getString("version"));
			builder.withDetail(SERVER, mongoTemplate.getDb().getMongo().getConnectPoint());
			addBuilder(builder);
		} else {
			builder.none();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			Assert.notNull(mongoTemplate, "MongoTemplate must not be null");
		} catch (Exception e) {
			valid = false;
		}
	}

}
