package planner.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Log4j2
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain,
        HttpSecurity> {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void configure(HttpSecurity http) {
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider);
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        log.debug("JwtTokenFilter added to HttpSecurity from JwtConfigurer");
    }
}
