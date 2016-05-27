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

import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.util.Assert;

import com.snowstore.mercury.core.health.Health;
import com.snowstore.mercury.core.indicator.AbstractHealthIndicator;
import com.snowstore.mercury.core.indicator.HealthIndicator;

/**
 * Simple implementation of a {@link HealthIndicator} returning status
 * information for Redis data stores.
 * 
 * @author sm
 */
public class RedisHealthIndicator extends AbstractHealthIndicator implements InitializingBean {

	@Autowired(required = false)
	private RedisConnectionFactory redisConnectionFactory;

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		if (valid) {
			RedisConnection connection = RedisConnectionUtils.getConnection(this.redisConnectionFactory);
			try {
				Properties info = connection.info();
				builder.up().withDetail("version", info.getProperty("redis_version"));
				if (redisConnectionFactory instanceof JedisConnectionFactory) {
					JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) redisConnectionFactory;
					builder.withDetail(SERVER, jedisConnectionFactory.getHostName() + ":" + jedisConnectionFactory.getPort() + ":" + jedisConnectionFactory.getDatabase());
				}
				addBuilder(builder);
			} finally {
				RedisConnectionUtils.releaseConnection(connection, this.redisConnectionFactory);
			}
		} else {
			builder.none();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {

			Assert.notNull(this.redisConnectionFactory, "ConnectionFactory must not be null");
			if (redisConnectionFactory instanceof JedisConnectionFactory) {
				if (((JedisConnectionFactory) redisConnectionFactory).getHostName().equals("localhost")) {
					throw new RuntimeException();
				}
			}
		} catch (Exception e) {
			valid = false;
		}
	}

}
