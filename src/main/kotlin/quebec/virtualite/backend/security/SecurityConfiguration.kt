package quebec.virtualite.backend.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import quebec.virtualite.backend.security.SecurityUsers.TEST_PASSWORD
import quebec.virtualite.backend.security.SecurityUsers.TEST_USER

@Configuration
@EnableWebSecurity
open class SecurityConfiguration
{
    @Bean
    @Throws(Exception::class)
    open fun filterChain(http: HttpSecurity): SecurityFilterChain
    {
        return http
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/css/**", "/i18n/**", "/js/**", "/*.html", "/").permitAll()
                    .anyRequest().authenticated()
            }
            .httpBasic(Customizer.withDefaults())
            .build()
    }

    @Bean
    open fun passwordEncoder(): PasswordEncoder
    {
        return BCryptPasswordEncoder()
    }

    @Bean
    open fun userDetailsService(encoder: PasswordEncoder): UserDetailsService
    {
        val admin = User
            .withUsername(TEST_USER)
            .password(encoder.encode(TEST_PASSWORD))
            .roles("ADMIN", "USER").build()

        return InMemoryUserDetailsManager(admin)
    }
}
