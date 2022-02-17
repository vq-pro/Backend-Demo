package quebec.virtualite.backend.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.CsrfTokenRepository
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.WebUtils
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
open class SecurityConfiguration(auth: AuthenticationManagerBuilder, dataSource: DataSource?) :
    WebSecurityConfigurerAdapter()
{
    private val X_XSRF_TOKEN = "X-XSRF-TOKEN"

    init
    {
        auth.jdbcAuthentication().dataSource(dataSource)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity)
    {
        http.httpBasic()
            .and().authorizeRequests()
            .antMatchers("/css/**", "/i18n/**", "/js/**").permitAll()
            .antMatchers("/*.html", "/").permitAll()
            .anyRequest().authenticated()
            .and().formLogin().loginPage("/login").permitAll()
            .and().logout()
            .and().addFilterAfter(CsrfHeaderFilter(), CsrfFilter::class.java)
            .csrf().csrfTokenRepository(csrfTokenRepository())
    }

    private fun csrfTokenRepository(): CsrfTokenRepository
    {
        val repository = HttpSessionCsrfTokenRepository()
        repository.setHeaderName(X_XSRF_TOKEN)
        return repository
    }

    private class CsrfHeaderFilter : OncePerRequestFilter()
    {
        private val XSRF_TOKEN = "XSRF-TOKEN"

        @Throws(ServletException::class, IOException::class)
        override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse, filterChain: FilterChain
        )
        {
            val csrf = request.getAttribute(
                CsrfToken::class.java
                    .name
            ) as CsrfToken

            var cookie = WebUtils.getCookie(request, XSRF_TOKEN)
            val token = csrf.token
            if (cookie == null || token != null && token != cookie.value)
            {
                cookie = Cookie(XSRF_TOKEN, token)
                cookie.path = "/"
                response.addCookie(cookie)
            }

            filterChain.doFilter(request, response)
        }
    }
}
