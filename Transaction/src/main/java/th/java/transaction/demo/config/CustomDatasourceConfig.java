package th.java.transaction.demo.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "customDbEntityManagerFactory", transactionManagerRef = "customDbTransactionManager", basePackages = {	"th.java.transaction.demo.db.repository" })
public class CustomDatasourceConfig {
	
	@Primary
	@Bean(name = "customDbDataSource")
	public DataSource customDbDataSource() throws Exception {
		HikariDataSource datasource = new HikariDataSource();
		datasource.setJdbcUrl("jdbc:mariadb://localhost:3306/DEMODB");
		datasource.setDriverClassName("org.mariadb.jdbc.Driver");
		datasource.setUsername("demousr");
		datasource.setPassword("demousr@");
		datasource.addDataSourceProperty("cachePrepStmts", true);
		datasource.addDataSourceProperty("prepStmtCacheSize", 250);
		datasource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
		datasource.addDataSourceProperty("useServerPrepStmts", true);
		datasource.setConnectionTimeout(20000);
		datasource.setValidationTimeout(10000);
		datasource.setMaximumPoolSize(10);
		datasource.setMinimumIdle(10);
		datasource.setMaxLifetime(180000);
		datasource.setIdleTimeout(10000);
		datasource.setAutoCommit(false);
		return datasource;
	}

	@Primary
	@Bean(name = "customDbEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean customDbEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("customDbDataSource") DataSource customDbDataSource) {
		return builder.dataSource(customDbDataSource).packages("th.java.transaction.demo.db.entity")
				.persistenceUnit("customDb").build();
	}

	@Primary
	@Bean(name = "customDbTransactionManager")
	public PlatformTransactionManager customDbTransactionManager(
			@Qualifier("customDbEntityManagerFactory") EntityManagerFactory customDbEntityManagerFactory) {
		return new JpaTransactionManager(customDbEntityManagerFactory);
	}

	@Primary
	@Bean
	@Qualifier("customDbJdbcTemplate")
	public JdbcTemplate customDbJdbcTemplate(@Qualifier("customDbDataSource") DataSource customDbDataSource) {
		return new JdbcTemplate(customDbDataSource);
	}
}
