package planner.controller;

import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import planner.exception.AuthenticationException;
import planner.model.User;
import planner.model.dto.request.UserLoginDto;
import planner.model.dto.request.UserRegistrationDto;
import planner.model.dto.response.UserResponseDto;
import planner.security.AuthenticationService;
import planner.security.jwt.JwtTokenProvider;
import planner.util.MapperUtil;

@RestController
@RequiredArgsConstructor
@Log4j2
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationDto registrationDto) {
        User user = authenticationService.register(
                registrationDto.getEmail(),
                registrationDto.getPassword(),
                registrationDto.getName(),
                registrationDto.getSurname());
        log.debug("Access register method from AuthenticationController for " + registrationDto);
        return MapperUtil.map(user, UserResponseDto.class);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid UserLoginDto userLoginDto)
            throws AuthenticationException {
        User user = authenticationService.login(userLoginDto.getLogin(),
                userLoginDto.getPassword());
        String token = jwtTokenProvider.createToken(user.getEmail(),
                user.getRoles().stream()
                        .map(r -> r.getRoleName().name())
                        .collect(Collectors.toList()));
        log.debug(String.format("Access login method from AuthenticationController for %s. "
                        + "JWT Token issued.", userLoginDto.getLogin()));
        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }
}
