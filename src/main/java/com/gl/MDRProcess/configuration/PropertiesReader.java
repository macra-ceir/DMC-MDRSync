package com.gl.MDRProcess.configuration;

import java.util.List;

import com.gl.MDRProcess.constant.DBType;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component

/*@PropertySources({
//	@PropertySource(value = {"classpath:application.properties"}),
//	@PropertySource(value = {"classpath:config.properties"})
})*/
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

	@Value("${eirs.alert.url}")
	public String alertUrl;

	@Value("${feature-name}")
	private String featureName;

	@Value("${dependent.feature-name}")
	private String dependentFeatureName;

	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;

	public DBType getDbType() {
		return driverClassName.startsWith("com.mysql") ? DBType.MYSQL : driverClassName.startsWith("oracle") ? DBType.ORACLE :
				DBType.NONE;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public List<String> getFilterDeviceTypes() {
		return filterDeviceTypes;
	}

	public void setFilterDeviceTypes(List<String> filterDeviceTypes) {
		this.filterDeviceTypes = filterDeviceTypes;
	}

	public String getAlertUrl() {
		return alertUrl;
	}

	public void setAlertUrl(String alertUrl) {
		this.alertUrl = alertUrl;
	}


	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getDependentFeatureName() {
		return dependentFeatureName;
	}

	public void setDependentFeatureName(String dependentFeatureName) {
		this.dependentFeatureName = dependentFeatureName;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	@Override
	public String toString() {
		return "PropertiesReader{" +
				"dialect='" + dialect + '\'' +
				", userId=" + userId +
				", userType=" + userType +
				", batchSize=" + batchSize +
				", moduleName='" + moduleName + '\'' +
				", feature='" + feature + '\'' +
				", filterDeviceTypes=" + filterDeviceTypes +
				", alertUrl='" + alertUrl + '\'' +
				", featureName='" + featureName + '\'' +
				", dependentFeatureName='" + dependentFeatureName + '\'' +
				", driverClassName='" + driverClassName + '\'' +
				'}';
	}
}
