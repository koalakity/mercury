package com.snowstore.mercury.jetty;

import java.util.Set;

import com.snowstore.mercury.core.EmbeddedHealthFactory;
import com.snowstore.mercury.core.indicator.HealthIndicator;

public class EmbeddedHealthJettyFactory implements EmbeddedHealthFactory {
	@Override
	public EmbeddedHealthJetty doStart(Set<HealthIndicator> healthIndicators, Integer port) {
		return new EmbeddedHealthJetty(healthIndicators, port);

	}
}
