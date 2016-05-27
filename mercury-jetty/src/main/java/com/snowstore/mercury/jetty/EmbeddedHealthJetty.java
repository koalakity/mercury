package com.snowstore.mercury.jetty;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Server;

import com.snowstore.mercury.core.EmbeddedHealthContainer;
import com.snowstore.mercury.core.indicator.HealthIndicator;
import com.snowstore.mercury.core.indicator.impl.ApplicationHealthIndicator;
import com.snowstore.mercury.core.indicator.impl.DiskSpaceHealthIndicator;
import com.snowstore.mercury.core.indicator.impl.DiskSpaceHealthIndicatorProperties;
import com.snowstore.mercury.core.indicator.impl.SystemHealthIndicator;
import com.snowstore.mercury.core.metric.SystemPublicMetrics;

public class EmbeddedHealthJetty implements EmbeddedHealthContainer {

	private static Log LOGGER = LogFactory.getLog(EmbeddedHealthJetty.class);

	private static final int DEFAULT_PORT = 7005;
	private Server server;

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public EmbeddedHealthJetty(Set<HealthIndicator> healthIndicators, Integer port) {
		try {
			if (null == port || port == 0)
				port = DEFAULT_PORT;
			server = new Server(port);
			healthIndicators.add(new DiskSpaceHealthIndicator(new DiskSpaceHealthIndicatorProperties()));
			healthIndicators.add(new ApplicationHealthIndicator());
			healthIndicators.add(new SystemHealthIndicator(new SystemPublicMetrics()));
			server.setHandler(new HealthHandler(healthIndicators));
			server.start();
		} catch (Exception e) {
			LOGGER.error("监控失败", e);
		}
	}

}
