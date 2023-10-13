package quebec.virtualite.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
public class TestUserDefinitions
{
    public static final String TEST_USER = "joe_user";
    public static final String TEST_PASSWORD = "123456";

    @Bean
    public MapReactiveUserDetailsService userDetailsService()
    {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username(TEST_USER)
            .password(TEST_PASSWORD)
            .roles("USER")
            .build();
        return new MapReactiveUserDetailsService(user);
    }
}
