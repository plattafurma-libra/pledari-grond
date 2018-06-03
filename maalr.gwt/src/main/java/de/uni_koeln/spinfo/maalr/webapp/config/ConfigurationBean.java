package de.uni_koeln.spinfo.maalr.webapp.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@Configuration
@ComponentScan(basePackages={"de.uni_koeln.spinfo.maalr"})
@PropertySource({"classpath:mongo.properties"})
public class ConfigurationBean {

}
