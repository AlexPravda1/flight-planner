package planner.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import planner.model.Aircraft;
import planner.model.json.plane.AircraftList;

import javax.sql.DataSource;
import java.util.Properties;

import static planner.config.template.DataSourceBeanConfig.*;
import static planner.config.template.SessionFactoryBeanConfig.*;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.*;

@Configuration
@ComponentScan(value = { "planner.dao.impl", "planner.security", "planner.service" })
@PropertySource("classpath:test.properties")
@RequiredArgsConstructor
public class TestConfig {
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
        properties.put(SHOW_SQL.name(), environment.getProperty(SHOW_SQL.value()));
        properties.put(HIBERNATE_DIALECT.value(),
                environment.getProperty(HIBERNATE_DIALECT.value()));
        properties.put(HIBERNATE_HBM2DDL.value(),
                environment.getProperty(HIBERNATE_HBM2DDL.value()));

        localSessionFactoryBean.setHibernateProperties(properties);
        localSessionFactoryBean.setPackagesToScan(PACKAGES_TO_SCAN.value());

        return localSessionFactoryBean;
    }

    @Bean
    public ObjectMapper getJsonMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Mapper getEntityMapper() {
        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        dozerBeanMapper.addMapping(aircraftMapperBuilder());
        return dozerBeanMapper;
    }

    @Bean
    public BeanMappingBuilder aircraftMapperBuilder() {
        return new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(AircraftList.class, Aircraft.class)
                        .fields(JSON_AIRCRAFT_ID.value(), MODEL_AIRCRAFT_ID.value())
                        .fields(JSON_IS_AIRCRAFT.value(), MODEL_IS_AIRCRAFT.value())
                        .fields(JSON_AIRCRAFT_TYPE.value(), MODEL_AIRCRAFT_TYPE.value())
                        .fields(JSON_AIRLINE_NAME.value(), MODEL_AIRLINE_NAME.value())
                        .fields(JSON_AIRLINE_ID.value(), MODEL_AIRLINE_ID.value())
                        .fields(JSON_AIRLINE_LEON_SUBDOMAIN.value(),
                                MODEL_AIRLINE_LEON_SUBDOMAIN.value());
            }
        };
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
