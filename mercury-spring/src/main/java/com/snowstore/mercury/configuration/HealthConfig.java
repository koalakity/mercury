package com.snowstore.mercury.configuration;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.snowstore.mercury.core.EmbeddedHealthContainer;
import com.snowstore.mercury.core.indicator.HealthIndicator;
import com.snowstore.mercury.indicator.DataSourceHealthIndicator;
import com.snowstore.mercury.indicator.EsbHealthIndicator;
import com.snowstore.mercury.indicator.GridFsHealthIndicator;
import com.snowstore.mercury.indicator.MemcachedHealthIndicator;
import com.snowstore.mercury.indicator.MongoHealthIndicator;
import com.snowstore.mercury.indicator.RedisHealthIndicator;
import com.snowstore.mercury.indicator.SolrHealthIndicator;
import com.snowstore.mercury.jetty.EmbeddedHealthJettyFactory;

@Configuration
public class HealthConfig {

	@Value("${management.port:7005}")
	private String port;

	@Value("${management.health.enabled:false}")
	private boolean enabled;

	@Autowired
	Set<HealthIndicator> healthIndicators;

	@Bean
	public EmbeddedHealthContainer embeddedHealthContainer() {
		if (!isEnabled()) {
			return null;
		}
		if (port == null || "".equals(port))
			return embeddeHealthJettyFactory().doStart(healthIndicators, null);
		else
			return embeddeHealthJettyFactory().doStart(healthIndicators, Integer.valueOf(port));

	}

	@Bean
	public EmbeddedHealthJettyFactory embeddeHealthJettyFactory() {
		return new EmbeddedHealthJettyFactory();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Bean
	public DataSourceHealthIndicator dataSourceHealthIndicator() {
		return new DataSourceHealthIndicator();
	}

	@Bean
	public RedisHealthIndicator redisHealthIndicator() {
		return new RedisHealthIndicator();
	}

	@Bean
	public MongoHealthIndicator mongoHealthIndicator() {
		return new MongoHealthIndicator();
	}

	@Bean
	public GridFsHealthIndicator gridFsHealthIndicator() {
		return new GridFsHealthIndicator();
	}

	@Bean
	public MemcachedHealthIndicator memcachedHealthIndicator() {
		return new MemcachedHealthIndicator();
	}

	@Bean
	public SolrHealthIndicator solrHealthIndicator() {
		return new SolrHealthIndicator();
	}
	
	@Bean
	public EsbHealthIndicator esbHealthIndicator() {
		return new EsbHealthIndicator();
	}
	
	
}