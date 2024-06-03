package ar.edu.itba.paw.persistence;

import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@ComponentScan({"ar.edu.itba.paw.persistence"})
@Configuration
public class TestConfig {

    @Value("classpath:pgsql.sql")
    private Resource pgsqlSql;

    @Value("classpath:schema.sql")
    private Resource schemaSql;

    @Value("classpath:testData.sql")
    private Resource testDataSql;

    @Bean
    public DataSource dataSource(){
        final SingleConnectionDataSource ds = new SingleConnectionDataSource();

        ds.setDriverClassName(JDBCDriver.class.getName());
        ds.setUrl("jdbc:hsqldb:mem:paw");
        ds.setUsername("ha");
        ds.setPassword("");

        ds.setSuppressClose(true);

        return ds;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource ds){
        final DataSourceInitializer dsi = new DataSourceInitializer();
        dsi.setDataSource(ds);
        dsi.setDatabasePopulator(databasePopulator());

        return dsi;
    }

    private DatabasePopulator databasePopulator(){
        final ResourceDatabasePopulator dbp = new ResourceDatabasePopulator();

        dbp.addScript(pgsqlSql);
        dbp.addScript(schemaSql);
        dbp.addScript(testDataSql);

        return dbp;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setPackagesToScan("ar.edu.ita.paw.webapp.models");
        emf.setDataSource(dataSource());

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");

        //FIXME: sacar antes del deploy
        jpaProperties.setProperty("hibernate.show_sql", "true");
        jpaProperties.setProperty("format_sql", "true");

        emf.setJpaProperties(jpaProperties);

        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf){
        return new JpaTransactionManager(emf);
    }
}
