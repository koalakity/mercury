/*
 * Copyright 2014-2015 the original author or authors.
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

package com.snowstore.mercury.core.indicator.impl;

import com.snowstore.mercury.core.health.Health;
import com.snowstore.mercury.core.health.Status;
import com.snowstore.mercury.core.indicator.AbstractHealthIndicator;
import com.snowstore.mercury.core.indicator.HealthIndicator;

/**
 * A {@link HealthIndicator} that checks available disk space and reports a
 * status of {@link Status#DOWN} when it drops below a configurable threshold.
 * 
 * @author Mattias Severson
 * @author Andy Wilkinson
 * @since 1.2.0
 */
public class DiskSpaceHealthIndicator extends AbstractHealthIndicator {

	private final DiskSpaceHealthIndicatorProperties properties;

	/**
	 * Create a new {@code DiskSpaceHealthIndicator}
	 * 
	 * @param properties
	 *            the disk space properties
	 */

	public DiskSpaceHealthIndicator(DiskSpaceHealthIndicatorProperties properties) {
		this.properties = properties;
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		long diskFreeInBytes = this.properties.getPath().getFreeSpace();

		builder.up();

		builder.withDetail("free", diskFreeInBytes);
	}
}
