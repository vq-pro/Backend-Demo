package quebec.virtualite.backend.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
    private static final String XSRF_TOKEN = "XSRF-TOKEN";
    private static final String X_XSRF_TOKEN = "X-XSRF-TOKEN";

    public SecurityConfiguration(AuthenticationManagerBuilder auth, DataSource dataSource)
        throws Exception
    {
        auth.jdbcAuthentication().dataSource(dataSource);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.httpBasic()
            .and().authorizeRequests()
            .antMatchers("/css/**", "/i18n/**", "/js/**").permitAll()
            .antMatchers("/*.html", "/").permitAll()
            .anyRequest().authenticated()

            .and().formLogin().loginPage("/login").permitAll()
            .and().logout()

            .and().addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
            .csrf().csrfTokenRepository(csrfTokenRepository());
    }

    private CsrfTokenRepository csrfTokenRepository()
    {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName(X_XSRF_TOKEN);
        return repository;
    }

    private static class CsrfHeaderFilter extends OncePerRequestFilter
    {
        @Override
        protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
        {
            CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
                .getName());

            if (csrf != null)
            {
                Cookie cookie = WebUtils.getCookie(request, XSRF_TOKEN);
                String token = csrf.getToken();
                if (cookie == null || token != null && !token.equals(cookie.getValue()))
                {
                    cookie = new Cookie(XSRF_TOKEN, token);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }

            filterChain.doFilter(request, response);
        }
    }
}
