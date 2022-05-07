package planner.config;

import static planner.config.ConfigProperty.DB_DRIVER;
import static planner.config.ConfigProperty.DB_PASSWORD;
import static planner.config.ConfigProperty.DB_URL;
import static planner.config.ConfigProperty.DB_USERNAME;
import static planner.config.ConfigProperty.HIBERNATE_DIALECT;
import static planner.config.ConfigProperty.HIBERNATE_HBM2DDL;
import static planner.config.ConfigProperty.HIBERNATE_SQL;
import static planner.config.ConfigProperty.PACKAGES_TO_SCAN;

import java.util.Properties;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "planner")
@RequiredArgsConstructor
public class AppConfig {
    private final Environment environment;

    @Bean
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty(DB_DRIVER.value()));
        dataSource.setUrl(environment.getProperty(DB_URL.value()));
        dataSource.setUsername(environment.getProperty(DB_USERNAME.value()));
        dataSource.setPassword(environment.getProperty(DB_PASSWORD.value()));
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(getDataSource());

        Properties properties = new Properties();
        properties.put("show_sql", environment.getProperty(HIBERNATE_SQL.value()));
        properties.put("hibernate.dialect",
                environment.getProperty(HIBERNATE_DIALECT.value()));
        properties.put("hibernate.hbm2ddl.auto",
                environment.getProperty(HIBERNATE_HBM2DDL.value()));

        localSessionFactoryBean.setHibernateProperties(properties);
        localSessionFactoryBean.setPackagesToScan(PACKAGES_TO_SCAN.value());

        return localSessionFactoryBean;
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    Mapper getMapper() {
        return new DozerBeanMapper();
    }
}
