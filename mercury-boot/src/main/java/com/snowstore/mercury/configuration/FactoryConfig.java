package com.snowstore.mercury.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.snowstore.mercury.core.EmbeddedHealthFactory;
import com.snowstore.mercury.jetty.EmbeddedHealthJettyFactory;
import com.snowstore.mercury.tomcat.EmbeddedHealthTomcatFactory;

@Configuration
public class FactoryConfig {

	@Autowired(required = false)
	TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory;

	@Autowired(required = false)
	JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory;

	@Bean
	public EmbeddedHealthFactory embeddeHealthFactory() {
		if (null != tomcatEmbeddedServletContainerFactory) {
			return new EmbeddedHealthTomcatFactory();
		}
		return new EmbeddedHealthJettyFactory();

	}

}
