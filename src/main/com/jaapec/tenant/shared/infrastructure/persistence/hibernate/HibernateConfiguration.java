package com.jaapec.tenant.shared.infrastructure.persistence.hibernate;

import java.util.Properties;
import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.jaapec.tenant.shared.infrastructure.Config;
import com.jaapec.tenant.shared.infrastructure.config.Parameter;
import com.jaapec.tenant.shared.infrastructure.config.ParameterNotExist;

@Configuration
public class HibernateConfiguration extends Config {

	private final Parameter config;

	public HibernateConfiguration(Parameter config) {
		this.config = config;
	}

	@Bean
	public DataSource dataSource() throws ParameterNotExist {
		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("org.mariadb.jdbc.Driver");
		dataSourceBuilder.url(
			String.format(
				"jdbc:mariadb://%s:%s/%s?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
				config.get("DATABASE_HOST"),
				config.get("DATABASE_PORT"),
				config.get("DATABASE_NAME")
			)
		);
		dataSourceBuilder.username(config.get("DATABASE_USER"));
		dataSourceBuilder.password(config.get("DATABASE_PASSWORD"));
		return dataSourceBuilder.build();
	}

	@Bean
	public PlatformTransactionManager transactionManager() throws ParameterNotExist {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

	private Properties hibernateProperties() throws ParameterNotExist {
		boolean debug = Boolean.parseBoolean(config.get("DEBUG"));
		Properties hibernateProperties = new Properties();
		hibernateProperties.put(AvailableSettings.HBM2DDL_AUTO, "update");
		hibernateProperties.put(AvailableSettings.SHOW_SQL, debug ? "true" : "false");
		return hibernateProperties;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws ParameterNotExist {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource());
		entityManagerFactory.setPackagesToScan("com.jaapec.tenant");
		entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactory.setJpaProperties(hibernateProperties());
		return entityManagerFactory;
	}
}
