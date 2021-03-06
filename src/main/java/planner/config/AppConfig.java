package planner.config;

import static planner.config.template.DataSourceBeanConfig.DB_DRIVER;
import static planner.config.template.DataSourceBeanConfig.DB_PASSWORD;
import static planner.config.template.DataSourceBeanConfig.DB_URL;
import static planner.config.template.DataSourceBeanConfig.DB_USERNAME;
import static planner.config.template.SessionFactoryBeanConfig.HIBERNATE_DIALECT;
import static planner.config.template.SessionFactoryBeanConfig.HIBERNATE_HBM2DDL;
import static planner.config.template.SessionFactoryBeanConfig.PACKAGES_TO_SCAN;
import static planner.config.template.SessionFactoryBeanConfig.SHOW_SQL;

import java.util.Properties;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "planner")
@RequiredArgsConstructor
@Log4j2
public class AppConfig {
    private final Environment environment;

    @Bean
    @Primary
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty(DB_DRIVER.value()));
        dataSource.setUrl(environment.getProperty(DB_URL.value()));
        dataSource.setUsername(environment.getProperty(DB_USERNAME.value()));
        dataSource.setPassword(environment.getProperty(DB_PASSWORD.value()));
        return dataSource;
    }

    @Bean
    @Primary
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(getDataSource());

        Properties properties = new Properties();
        properties.put(SHOW_SQL.name(), environment.getProperty(SHOW_SQL.value()));
        properties.put(HIBERNATE_DIALECT.value(),
                environment.getProperty(HIBERNATE_DIALECT.value()));
        properties.put(HIBERNATE_HBM2DDL.value(),
                environment.getProperty(HIBERNATE_HBM2DDL.value()));

        localSessionFactoryBean.setHibernateProperties(properties);
        localSessionFactoryBean.setPackagesToScan(PACKAGES_TO_SCAN.value());
        log.debug("Provided LocalSessionFactoryBean from config/AppConfig");
        return localSessionFactoryBean;
    }

    @Bean
    public PasswordEncoder getEncoder() {
        log.debug("Provided PasswordEncoder Bean from config/AppConfig");
        return new BCryptPasswordEncoder();
    }
}
