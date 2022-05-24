package planner.config;

import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.JSON_AIRCRAFT_ID;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.JSON_AIRCRAFT_TYPE;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.JSON_AIRLINE_ID;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.JSON_AIRLINE_LEON_SUBDOMAIN;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.JSON_AIRLINE_NAME;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.JSON_IS_AIRCRAFT;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.MODEL_AIRCRAFT_ID;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.MODEL_AIRCRAFT_TYPE;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.MODEL_AIRLINE_ID;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.MODEL_AIRLINE_LEON_SUBDOMAIN;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.MODEL_AIRLINE_NAME;
import static planner.config.template.mapper.config.JsonMapperAircraftFieldsConfig.MODEL_IS_AIRCRAFT;
import static planner.config.template.mapper.config.MapperAircraftResponseDtoFieldsConfig.AIRCRAFT_AIRLINE_NAME;
import static planner.config.template.mapper.config.MapperAircraftResponseDtoFieldsConfig.AIRCRAFT_RESPONSE_DTO_AIRLINE_NAME;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import planner.model.Aircraft;
import planner.model.dto.response.AircraftResponseDto;
import planner.model.json.plane.AircraftList;

@Configuration
@ComponentScan(basePackages = "planner")
@RequiredArgsConstructor
public class MapperConfig {
    @Bean
    public ObjectMapper getJsonMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Mapper getEntityMapper() {
        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        dozerBeanMapper.addMapping(aircraftMapperBuilder());
        dozerBeanMapper.addMapping(aircraftDtoMapperBuilder());
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
    public BeanMappingBuilder aircraftDtoMapperBuilder() {
        return new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(Aircraft.class, AircraftResponseDto.class)
                        .fields(AIRCRAFT_AIRLINE_NAME.value(),
                                AIRCRAFT_RESPONSE_DTO_AIRLINE_NAME.value());
            }
        };
    }
}
