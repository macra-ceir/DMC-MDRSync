package com.gl.MDRProcess.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@PropertySources({
	@PropertySource(value = {"classpath:application.properties"}),
//	@PropertySource(value = {"classpath:config.properties"})
})
public class PropertiesReader {

	@Value("${spring.jpa.properties.hibernate.dialect}")
	public String dialect;
	
	@Value("#{new Integer('${process.userId}')}")
	public Integer userId;
	
	@Value("#{new Integer('${process.userType}')}")
	public Integer userType;
	
	@Value("#{new Integer('${process.batchSize}')}")
	public int batchSize;
	
	@Value("${process.moduleName}")
	public String moduleName;
	
	@Value("${process.feature}")
	public String feature;
	
	@Value("#{'${filter.deviceTypes}'.split(',')}")
	public List<String> filterDeviceTypes;
	
}
