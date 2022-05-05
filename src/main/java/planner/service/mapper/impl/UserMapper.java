package planner.service.mapper.impl;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import planner.model.User;
import planner.model.dto.request.UserRegistrationDto;
import planner.model.dto.response.RoleResponseDto;
import planner.model.dto.response.UserResponseDto;
import planner.service.mapper.DtoRequestMapper;
import planner.service.mapper.DtoResponseMapper;

@RequiredArgsConstructor
@Component
public class UserMapper implements DtoRequestMapper<UserRegistrationDto, User>,
                                   DtoResponseMapper<UserResponseDto, User> {
    private final RoleMapper roleMapper;

    @Override
    public User toEntity(UserRegistrationDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setRoles(new HashSet<>());
        return user;
    }

    @Override
    public UserResponseDto toDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setEmail(user.getEmail());
        List<RoleResponseDto> roles = user.getRoles()
                .stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
        dto.setRoles(roles);
        return dto;
    }
}
