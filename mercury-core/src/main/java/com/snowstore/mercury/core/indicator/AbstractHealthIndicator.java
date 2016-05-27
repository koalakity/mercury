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

package com.snowstore.mercury.core.indicator;

import com.snowstore.mercury.core.health.Health;
import com.snowstore.mercury.core.health.Status;
import com.snowstore.mercury.core.health.Health.Builder;

/**
 * Base {@link HealthIndicator} implementations that encapsulates creation of
 * {@link Health} instance and error handling.
 * <p>
 * This implementation is only suitable if an {@link Exception} raised from
 * {@link #doHealthCheck(org.mercury.jetty.health.boot.actuate.health.Health.Builder)}
 * should create a {@link Status#DOWN} health status.
 * 
 * @author Christian Dupuis
 * @since 1.1.0
 */
public abstract class AbstractHealthIndicator implements HealthIndicator {
	
	protected static final String SERVER = "server";


	protected boolean valid = true;

	/** 自定义参数 */
	public void addBuilder(Builder builder) {
		return;
	}

	@Override
	public final Health health() {
		Health.Builder builder = new Health.Builder();
		try {
			doHealthCheck(builder);
		} catch (Exception ex) {
			builder.down(ex);
		}
		return builder.build();
	}

	/**
	 * Actual health check logic.
	 * 
	 * @param builder
	 *            the {@link Builder} to report health status and details
	 * @throws Exception
	 *             any {@link Exception} that should create a
	 *             {@link Status#DOWN} system status.
	 */
	protected abstract void doHealthCheck(Health.Builder builder) throws Exception;
}
