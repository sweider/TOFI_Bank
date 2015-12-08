package by.bsuir.sweider_b.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.util.Properties;

/**
 * Created by sweid on 06.12.2015.
 */

@Configuration
@PropertySource("classpath:db.properties")
public class HibernateConfig{
    private final static Logger LOGGER = LoggerFactory.getLogger(HibernateConfig.class);

    @Bean(name = {"sessionFactory"})
    @Autowired
    public LocalSessionFactoryBean getSessionFactory(BasicDataSource dataSource) {

        LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setHibernateProperties(additionalProperties());
        bean.setPackagesToScan("by.bsuir.sweider_b");
        return bean;
    }

    @Bean(destroyMethod = "close", name = {"dataSource"})
    @Autowired
    public BasicDataSource getDataSource(Environment env) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        StringBuilder sb = new StringBuilder("jdbc:postgresql://");
        sb.append(env.getProperty("db.ip")).append(":").append(env.getProperty("db.port")).append("/").append(env.getProperty("db.name"));
        ds.setUrl(sb.toString());
        ds.setUsername(env.getProperty("db.username"));
        ds.setPassword(env.getProperty("db.password"));
        return ds;
    }



    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
        properties.setProperty("hibernate.id.new_generator_mappings", "true");
        properties.setProperty("hibernate.current_session_context_class", "thread");
//        properties.setProperty("hibernate.connection.username", "postgres");
//        properties.setProperty("hibernate.connection.password", "postgres");
//        properties.setProperty("hibernate.show_sql", "true");
//        properties.setProperty("hibernate.format_sql", "true");
        return properties;
    }

}
