package planner.controller;

import static planner.config.template.EndpointConfig.INDEX;

import java.net.URI;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import planner.util.SecurityCipher;

@RestController
@RequiredArgsConstructor
@Log4j2
public class AuthenticationController {
    @Value("${security.jwt.cookie.token}")
    private String jwtCookieName;
    @Value("${security.jwt.cookie.validity.seconds}")
    private int jwtCookieValidity;
    private final AuthenticationService authenticationService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationDto registrationDto) {
        log.debug("Access register method from AuthenticationController for "
                + registrationDto.getEmail());
        User user = authenticationService.register(
                registrationDto.getEmail(),
                registrationDto.getPassword(),
                registrationDto.getName(),
                registrationDto.getSurname());
        return MapperUtil.map(user, UserResponseDto.class);
    }

    @PostMapping("/auth")
    public ResponseEntity<String> auth(
            @Valid @ModelAttribute("UserLoginDto") UserLoginDto userLoginDto)
            throws AuthenticationException {
        log.debug("/auth from AuthenticationController is called for "
                + userLoginDto.getLogin());
        String token = getUserAccessToken(userLoginDto);
        HttpHeaders responseHeaders = getHttpHeadersWithEncryptedCookiesAndUrlRedirect(token);
        return new ResponseEntity<>(responseHeaders, HttpStatus.FOUND);
    }

    private HttpHeaders getHttpHeadersWithEncryptedCookiesAndUrlRedirect(String token) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpCookie httpCookie = ResponseCookie
                .from(jwtCookieName, SecurityCipher.encrypt(token))
                .maxAge(jwtCookieValidity)
                .httpOnly(true)
                //.secure(true)
                .path("/")
                .build();
        responseHeaders.add(HttpHeaders.SET_COOKIE, httpCookie.toString());
        responseHeaders.setLocation(URI.create(INDEX.value()));
        return responseHeaders;
    }

    private String getUserAccessToken(UserLoginDto userLoginDto) throws AuthenticationException {
        User user = authenticationService.login(userLoginDto.getLogin(),
                userLoginDto.getPassword());
        return jwtTokenProvider.createToken(user.getEmail(),
                user.getRoles().stream()
                        .map(r -> r.getRoleName().name())
                        .collect(Collectors.toList()));
    }
}
