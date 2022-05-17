package planner.config;

import static planner.config.template.DataSourceBeanConfig.DB_DRIVER;
import static planner.config.template.DataSourceBeanConfig.DB_PASSWORD;
import static planner.config.template.DataSourceBeanConfig.DB_URL;
import static planner.config.template.DataSourceBeanConfig.DB_USERNAME;
import static planner.config.template.SessionFactoryBeanConfig.HIBERNATE_DIALECT;
import static planner.config.template.SessionFactoryBeanConfig.HIBERNATE_HBM2DDL;
import static planner.config.template.SessionFactoryBeanConfig.PACKAGES_TO_SCAN;
import static planner.config.template.SessionFactoryBeanConfig.SHOW_SQL;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.JSON_AIRCRAFT_ID;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.JSON_AIRCRAFT_TYPE;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.JSON_AIRLINE_ID;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.JSON_AIRLINE_LEON_SUBDOMAIN;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.JSON_AIRLINE_NAME;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.JSON_IS_ACTIVE;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.JSON_IS_AIRCRAFT;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.MODEL_AIRCRAFT_ID;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.MODEL_AIRCRAFT_TYPE;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.MODEL_AIRLINE_ID;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.MODEL_AIRLINE_LEON_SUBDOMAIN;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.MODEL_AIRLINE_NAME;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.MODEL_IS_ACTIVE;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.MODEL_IS_AIRCRAFT;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Properties;
import javax.sql.DataSource;
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
                                MODEL_AIRLINE_LEON_SUBDOMAIN.value())
                        .fields(field(JSON_IS_ACTIVE.value()).accessible(),
                                field(MODEL_IS_ACTIVE.value()).accessible());
            }
        };
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
