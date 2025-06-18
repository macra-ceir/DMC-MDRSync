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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = {"com.gl.MDRProcess.repo.audit"},
		entityManagerFactoryRef = "auditEntityManagerFactory",
		transactionManagerRef = "auditTransactionManager" ,
		repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public class AuditDbConfig {

	@Bean(name = "auditEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean auditEntityManagerFactory(
			@Qualifier("auditDataSource") DataSource dataSource,
			EntityManagerFactoryBuilder builder) {
		return builder
				.dataSource(dataSource)
				.packages("com.gl.MDRProcess.model.audit")
				.persistenceUnit("aud")
				.properties(jpaProperties())
				.build();
	}

	@Bean(name = "auditDataSource")
	@ConfigurationProperties(prefix = "audit.datasource")
	public DataSource auditDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "auditTransactionManager")
	public PlatformTransactionManager auditTransactionManager(
			@Qualifier("auditEntityManagerFactory") LocalContainerEntityManagerFactoryBean auditEntityManagerFactory) {
		return new JpaTransactionManager(Objects.requireNonNull(auditEntityManagerFactory.getObject()));
	}


	protected Map<String, Object> jpaProperties() {
		Map<String, Object> props = new HashMap<>();
		props.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
		props.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
		return props;
	}
}

