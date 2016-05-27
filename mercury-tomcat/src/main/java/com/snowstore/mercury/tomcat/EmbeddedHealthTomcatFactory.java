package com.snowstore.mercury.tomcat;

import java.util.Set;

import com.snowstore.mercury.core.EmbeddedHealthFactory;
import com.snowstore.mercury.core.indicator.HealthIndicator;

public class EmbeddedHealthTomcatFactory implements EmbeddedHealthFactory {
	@Override
	public EmbeddedHealthTomcat doStart(Set<HealthIndicator> healthIndicators, Integer port) {
		return new EmbeddedHealthTomcat(healthIndicators, port);

	}
}
