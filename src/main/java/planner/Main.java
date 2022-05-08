package planner;

import java.util.List;
import java.util.Set;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import planner.config.enums.SessionFactoryBeanConfig;
import planner.config.enums.UserRoleName;
import planner.model.Role;
import planner.model.User;
import planner.model.dto.response.RoleResponseDto;
import planner.model.dto.response.UserResponseDto;

public class Main {
    private static final Mapper mapper = new DozerBeanMapper();

    public static void main(String[] args) {
        System.out.println("Hello");

        User user = new User();
        user.setId(1L);
        user.setName("boba");
        user.setSurname("boba");
        user.setPassword("supaPasaward");
        Role roleUser = new Role(UserRoleName.USER);
        roleUser.setId(2L);
        user.setRoles(Set.of(roleUser));

        UserResponseDto responseDto = mapper.map(user, UserResponseDto.class);
        System.out.println("UserDto " + responseDto);

        List<RoleResponseDto> roles = responseDto.getRoles();
        System.out.println("RR DTO " + roles);

    }
}
