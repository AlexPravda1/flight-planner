package planner.service.mapper.impl;

import org.springframework.stereotype.Component;
import planner.model.Role;
import planner.model.dto.response.RoleResponseDto;
import planner.service.mapper.DtoResponseMapper;

@Component
public class RoleMapper implements DtoResponseMapper<RoleResponseDto, Role> {
    @Override
    public RoleResponseDto toDto(Role role) {
        RoleResponseDto dto = new RoleResponseDto();
        dto.setId(role.getId());
        dto.setName(role.getRoleName().name());
        return dto;
    }
}
