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

package com.snowstore.mercury.core.indicator.impl;

import com.snowstore.mercury.core.health.Health;
import com.snowstore.mercury.core.health.Status;
import com.snowstore.mercury.core.indicator.AbstractHealthIndicator;
import com.snowstore.mercury.core.indicator.HealthIndicator;

/**
 * Default implementation of {@link HealthIndicator} that returns {@link Status#UP}.
 *
 * @author Dave Syer
 * @author Christian Dupuis
 * @see Status#UP
 */
public class ApplicationHealthIndicator extends AbstractHealthIndicator {

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		builder.up();
	}

}
