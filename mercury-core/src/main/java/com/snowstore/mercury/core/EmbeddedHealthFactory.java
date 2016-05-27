package com.snowstore.mercury.core;

import java.util.Set;

import com.snowstore.mercury.core.indicator.HealthIndicator;

public interface EmbeddedHealthFactory {
	public EmbeddedHealthContainer doStart(Set<HealthIndicator> healthIndicators, Integer port);

}
