package planner.config;

import static planner.config.template.EndpointConfig.INDEX;
import static planner.config.template.EndpointConfig.LOGIN;
import static planner.config.template.EndpointConfig.REGISTER;
import static planner.config.template.EndpointConfig.TEST;
import static planner.config.template.EndpointConfig.WELCOME;
import static planner.config.template.UserRoleName.ADMIN;
import static planner.config.template.UserRoleName.USER;

import lombok.RequiredArgsConstructor;
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
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()

                .antMatchers(HttpMethod.GET,
                        TEST.value()).hasRole(ADMIN.value())

                .antMatchers(HttpMethod.GET,
                        INDEX.value())
                .hasAnyRole(ADMIN.value(), USER.value())

                .antMatchers(HttpMethod.POST,
                        REGISTER.value(),
                        LOGIN.value()).permitAll()

                .antMatchers(WELCOME.value()).permitAll()

                .antMatchers(HttpMethod.DELETE).hasRole(ADMIN.value())
                .antMatchers(HttpMethod.PUT).hasRole(ADMIN.value())

                .anyRequest().authenticated()

                .and()
                .formLogin().disable()

                .apply(new JwtConfigurer(jwtTokenProvider))

                .and()
                .headers().frameOptions().disable();
    }
}
