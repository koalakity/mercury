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
import org.springframework.util.Assert;

import com.snowstore.mercury.core.health.Health;
import com.snowstore.mercury.core.indicator.AbstractHealthIndicator;
import com.snowstore.mercury.core.indicator.HealthIndicator;
import com.zendaimoney.hera.connector.EsbConnector;
import com.zendaimoney.hera.connector.vo.Datagram;

/**
 * Simple implementation of a {@link HealthIndicator} returning status
 * information for esb(amq)
 * 
 * @author sm
 */
public class EsbHealthIndicator extends AbstractHealthIndicator implements InitializingBean {

	@Autowired(required = false)
	private EsbConnector esbConnector;

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		if (valid) {
			builder.up().withDetail("system.code", esbConnector.getSystemCode()).withDetail(SERVER, esbConnector.getEsbUrl()).withDetail("count", esbConnector.getCount());
			esbConnector.send(new Datagram());
			addBuilder(builder);
		} else {
			builder.none();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			Assert.notNull(this.esbConnector, "esbConnector must not be null");
		} catch (Exception e) {
			valid = false;
		}
	}

}
