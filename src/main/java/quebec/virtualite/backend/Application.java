package quebec.virtualite.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import quebec.virtualite.backend.security.SecurityUserManager;

import static quebec.virtualite.backend.security.SecurityUsers.TEST_PASSWORD;
import static quebec.virtualite.backend.security.SecurityUsers.TEST_USER;

@SpringBootApplication(scanBasePackages = {"quebec.virtualite.*"})
@Slf4j
public class Application
{
    static
    {
        log.warn("STARTING...");
    }

    public Application(SecurityUserManager userManager)
    {
        userManager.defineUser(TEST_USER, TEST_PASSWORD);
        log.warn("STARTED");
    }

    public static void main(final String[] args)
    {
        SpringApplication.run(Application.class, args);
    }
}
