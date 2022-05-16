package planner.config;

import static planner.config.template.MapperFieldsConfig.JSON_AIRCRAFT_ID;
import static planner.config.template.MapperFieldsConfig.JSON_AIRCRAFT_TYPE;
import static planner.config.template.MapperFieldsConfig.JSON_IS_AIRCRAFT;
import static planner.config.template.MapperFieldsConfig.MODEL_AIRCRAFT_ID;
import static planner.config.template.MapperFieldsConfig.MODEL_IS_AIRCRAFT;
import static planner.config.template.MapperFieldsConfig.MODE_AIRCRAFT_TYPE;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import planner.model.Aircraft;
import planner.model.json.AircraftList;

@Configuration
@ComponentScan(basePackages = "planner")
@RequiredArgsConstructor
public class MapperConfig {
    @Bean
    public Mapper getPojoModelMapper() {
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
                        .fields(JSON_AIRCRAFT_TYPE.value(), MODE_AIRCRAFT_TYPE.value());
            }
        };
    }

    @Bean
    public ObjectMapper getJsonJavaMapper() {
        return new ObjectMapper();
    }
}
