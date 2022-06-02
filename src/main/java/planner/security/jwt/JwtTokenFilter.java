package planner.security.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
@Log4j2
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        log.debug("Trying to get token from HTTP Header: " + (token == null ? "FAIL" : "SUCCESS"));

        if (token == null) {
            token = jwtTokenProvider.getJwtFromCookie((HttpServletRequest) servletRequest);
            log.debug("Trying to get token from COOKIE Header: "
                    + (token == null ? "FAIL" : "SUCCESS"));
        }

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.debug("Authentication done and added to SecurityContextHolder"
                    + " based on valid JWT Token.");
        }
        log.debug(String.format("Was provided %s jwt Token", token == null ? "WRONG" : "VALID"));
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
