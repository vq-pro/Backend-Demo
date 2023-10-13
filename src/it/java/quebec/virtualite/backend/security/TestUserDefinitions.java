package quebec.virtualite.backend.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;

@Configuration
@Slf4j
public class TestUserDefinitions
{
    public static final String TEST_USER = "joe_user";
    public static final String TEST_PASSWORD = "123456";

    @Bean
    public MapReactiveUserDetailsService userDetailsService()
    {
        log.warn("User.withDefaultPasswordEncoder() is used for testing");
        return new MapReactiveUserDetailsService(
            User.withDefaultPasswordEncoder()
                .username(TEST_USER)
                .password(TEST_PASSWORD)
                .roles("USER")
                .build());
    }
}
