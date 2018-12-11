package com.zhtangsh.SpringDataMultiTenantMySQL.master.config;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Created by Zihan Eric Tang(mrzihan.tang@gmail.com) on 2018/12/11
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {
        "com.zhtangsh.SpringDataMultiTenantMySQL.master.model",
        "com.zhtangsh.SpringDataMultiTenantMySQL.master.repository"},
        entityManagerFactoryRef = "masterEntityManagerFactory", transactionManagerRef = "masterTransactionManager")
public class MasterDatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(MasterDatabaseConfig.class);

    @Bean(name = "masterDataSource")
    public DataSource masterDataSource() {
        logger.info("Start setting up master datasource");
        HikariDataSource ds = new HikariDataSource();
        ds.setUsername("root");
        ds.setPassword("root");
        ds.setJdbcUrl("jdbc:mysql://10.19.140.200:32143/master?createDatabaseIfNotExist=true");
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setConnectionTimeout(20000);
        ds.setMaximumPoolSize(5);
        ds.setMinimumIdle(5);
        ds.setIdleTimeout(300000);
        ds.setPoolName("master-connection-pool");
        ds.setConnectionTestQuery("SELECT 1");
        logger.info("Finish setting up master datasource");
        return ds;
    }

    @Primary
    @Bean(name = "masterEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory() {
        logger.info("Start setting up master EntityManagerFactory");
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(masterDataSource());
        em.setPackagesToScan(new String[]{
                "com.zhtangsh.SpringDataMultiTenantMySQL.master.model",
                "com.zhtangsh.SpringDataMultiTenantMySQL.master.repository"
        });
        em.setPersistenceUnitName("master-persistence-unit");
        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(jpaVendorAdapter);
        em.setJpaProperties(hibernateProperties());
        logger.info("Finish setting up master EntityManagerFactory");
        return em;
    }

    @Bean(name = "masterTransactionManager")
    public JpaTransactionManager masterTransactionmanager(
            @Qualifier("masterEntityManagerFactory") EntityManagerFactory emf
    ) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(emf);
        return jpaTransactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.FORMAT_SQL, true);
        properties.put(Environment.HBM2DDL_AUTO, "update");
        return properties;
    }
}
