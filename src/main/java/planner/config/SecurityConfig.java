package planner.config;

import static planner.config.template.EndpointConfig.LOGIN;
import static planner.config.template.EndpointConfig.REGISTER;
import static planner.config.template.EndpointConfig.TEST;
import static planner.config.template.EndpointConfig.USER_AUTH;
import static planner.config.template.EndpointConfig.USER_LOGIN;
import static planner.model.UserRoleName.ADMIN;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import planner.security.jwt.JwtConfigurer;
import planner.security.jwt.JwtTokenProvider;

@EnableWebSecurity
@RequiredArgsConstructor
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${security.jwt.cookie.token}")
    private String jwtCookieName;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.debug("SecurityConfig access AuthenticationManagerBuilder "
                + "in Configure method to authorize user via userDetailsService");
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .formLogin().loginPage(USER_LOGIN.value()).permitAll()
                    .and()
                .logout().clearAuthentication(true)
                    .logoutSuccessUrl(USER_LOGIN.value())
                    .deleteCookies(jwtCookieName)

                .and()
                .authorizeRequests()

                .mvcMatchers(LOGIN.value()).permitAll()
                .mvcMatchers(USER_LOGIN.value()).permitAll()
                .mvcMatchers(USER_AUTH.value()).permitAll()
                .mvcMatchers(REGISTER.value()).permitAll()

                .antMatchers(TEST.value()).hasRole(ADMIN.value())
                .antMatchers(HttpMethod.DELETE).hasRole(ADMIN.value())
                .antMatchers(HttpMethod.PUT).hasRole(ADMIN.value())

                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider))
                .and()
                .headers().frameOptions().disable();
    }
}
