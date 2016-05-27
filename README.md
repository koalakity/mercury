# mercury
提供系统以及依赖资源活动监测，基于spring spring-boot-actuator,添加非spring boot项目 和非spring项目支持
1 安装
A:适用于spring 

<dependency>
		<groupId>com.snowstore.mercury</groupId>
		<artifactId>mercury-spring</artifactId>
		<version>1.0.0-SNAPSHOT</version>
</dependency>

B:适用于boot 

<dependency>
		<groupId>com.snowstore.mercury</groupId>
		<artifactId>mercury-boot</artifactId>
		<version>1.0.0-SNAPSHOT</version>
</dependency>
C:适用于普通java

<dependency>
		<groupId>com.snowstore.mercury</groupId>
		<artifactId>mercury-jetty</artifactId>
		<version>1.0.0-SNAPSHOT</version>
</dependency>
2 配置
A:XML方式：适用于spring

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:management="http://www.snowstore.com/schema/management"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd  
    http://www.snowstore.com/schema/management http://www.snowstore.com/schema/management/management.xsd">
	<!-- 监控配置-->
	<management:health/>
</beans>
B:注解方式：适用于boot

@EnableHealth
@Configuration
public class HealthConfiguration {

}
配置文件

management.health.enabled=true #允许监控开关，不填为关
management.port=7005 #监控端口，可不填，默认为7005
C:  普通方式：适用于普通java

Set<HealthIndicator> healthIndicators = new HashSet<HealthIndicator>();
new EmbeddedHealthJettyFactory().doStart(healthIndicators , null);
3 使用
已实现默认和扩展监控项

@Component
public class MyHealthIndicator implements HealthIndicator {
	@Override
	public Health health() {
		int errorCode = check(); // perform some specific health check
		if (errorCode != 0) {
			return Health.down().withDetail("Error Code", errorCode).build();
		}
		return Health.up().build();
	}
}
如果想在已有的扩展监控中增加项目，可实现扩展类，注意名字请保持一致。

@Component
public class DataSourceHealthIndicator extends com.snowstore.mercury.indicator.DataSourceHealthIndicator {
	@Autowired
	private DataSource dataSource;
	@Override
	public void addBuilder(Builder builder) {
		builder.withDetail("connection.count", dataSource.getPoolSize());
	}
}
启动后，使用浏览器打开url：http://IP:7005。
{"status":"UP","my":{"status":"UP"},"system":{"status":"UP","mem":4019584,"mem.free":3733048,"processors":4,"instance.uptime":8170,"uptime":17579,"systemload.average":-1.0,"heap.committed":4019584,"heap.init":4194304,"heap.used":286535,"heap":4019584,"threads.peak":55,"threads.daemon":34,"threads":55,"classes":9923,"classes.loaded":9923,"classes.unloaded":0,"gc.parnew.count":1,"gc.parnew.time":59,"gc.concurrentmarksweep.count":1,"gc.concurrentmarksweep.time":325,"noheap.committed":527936,"noheap.init":526784,"noheap.used":57979,"noheap":573440},"diskSpace":{"status":"UP","free":133413724160,"threshold":10485760},"redis":{"status":"UP","version":"3.0.2"},"mongo":{"status":"UP","version":"2.6.5"}}
