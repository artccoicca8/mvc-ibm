package com.alignet.vadmin.spring.configuration;

import java.util.Properties;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {
		"com.alignet.vadmin.spring.business.repositories" }, entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
@EnableTransactionManagement
@EnableSpringDataWebSupport
public class WebAppPersistenceConfig {

	@Bean
	public DataSource dataSource() throws NamingException {
		JndiObjectFactoryBean jndiObjectFactory = new JndiObjectFactoryBean();
		jndiObjectFactory.setJndiName("java:comp/env/jdbc/UWDES");
		jndiObjectFactory.setLookupOnStartup(false);
		jndiObjectFactory.setCache(true);
		jndiObjectFactory.setProxyInterface(DataSource.class);
		jndiObjectFactory.afterPropertiesSet();
		return (DataSource) jndiObjectFactory.getObject();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws NamingException {
		Properties props = new Properties();
		props.put("hibernate.default_schema", "SQMPP");
		props.put("hibernate.generate_statistics", "true");
		props.put("hibernate.connection.isolation", "1");
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource());
		entityManagerFactory.setPackagesToScan("com.alignet.vadmin.spring.business.entities");

		HibernateJpaVendorAdapter hibernateJpaVendor = new HibernateJpaVendorAdapter();
		hibernateJpaVendor.setGenerateDdl(true);
		hibernateJpaVendor.setShowSql(true);
		hibernateJpaVendor.setDatabasePlatform("org.hibernate.dialect.DB2Dialect");
		hibernateJpaVendor.setDatabase(Database.DB2);

		entityManagerFactory.setJpaVendorAdapter(hibernateJpaVendor);
		entityManagerFactory.setJpaProperties(props);
		return entityManagerFactory;
	}

	@Bean
	public JpaTransactionManager transactionManager() throws NamingException {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory().getObject());
		txManager.setJpaDialect(new HibernateJpaDialect());
		return txManager;
	}

 

}
