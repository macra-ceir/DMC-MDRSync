package com.gl.MDRProcess;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = {"com.gl.MDRProcess.repo.app"},
		entityManagerFactoryRef = "appEntityManagerFactory",
		transactionManagerRef = "appTransactionManager",
		repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public class AppDbConfig {

	@Primary
	@Bean(name = "appEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean appEntityManagerFactory(
			@Qualifier("appDataSource") DataSource dataSource,
			EntityManagerFactoryBuilder builder) {
		return builder
				.dataSource(dataSource)
				.packages("com.gl.MDRProcess.model.app")
				.persistenceUnit("app")
				.properties(jpaProperties())
				.build();

	}

	@Primary
	@Bean(name = "appTransactionManager")
	public PlatformTransactionManager appTransactionManager(
			@Qualifier("appEntityManagerFactory") LocalContainerEntityManagerFactoryBean appEntityManagerFactory) {
		return new JpaTransactionManager(Objects.requireNonNull(appEntityManagerFactory.getObject()));
	}

	// DataSource Configs
	@Primary
	@Bean(name = "appDataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource appDataSource() {
		return DataSourceBuilder.create().build();
	}


	protected Map<String, Object> jpaProperties() {
		Map<String, Object> props = new HashMap<>();
		props.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
		props.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
//		props.put("hibernate.hbm2ddl.auto", "update");
		return props;
	}

}
