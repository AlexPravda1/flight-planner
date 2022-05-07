package planner.config;

import static planner.config.ConfigProperty.POINT_INDEX;
import static planner.config.ConfigProperty.POINT_LOGIN;
import static planner.config.ConfigProperty.POINT_REGISTER;
import static planner.config.ConfigProperty.POINT_TEST;
import static planner.config.ConfigProperty.ROLE_ADMIN;
import static planner.config.ConfigProperty.ROLE_USER;

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
                        POINT_TEST.getValue()).hasRole(ROLE_ADMIN.getValue())

                .antMatchers(HttpMethod.GET,
                        POINT_INDEX.getValue())
                .hasAnyRole(ROLE_ADMIN.getValue(), ROLE_USER.getValue())

                .antMatchers(HttpMethod.POST,
                        POINT_REGISTER.getValue(),
                        POINT_LOGIN.getValue()).permitAll()

                .antMatchers(HttpMethod.DELETE).hasRole(ROLE_ADMIN.getValue())
                .antMatchers(HttpMethod.PUT).hasRole(ROLE_ADMIN.getValue())

                .anyRequest().authenticated()

                .and()
                .formLogin().disable()

                .apply(new JwtConfigurer(jwtTokenProvider))

                .and()
                .headers().frameOptions().disable();
    }
}
