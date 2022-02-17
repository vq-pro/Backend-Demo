package quebec.virtualite.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import quebec.virtualite.backend.security.SecurityUserManager;
import quebec.virtualite.backend.security.SecurityUsers;

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
        userManager.defineUser(
            SecurityUsers.INSTANCE.getTEST_USER(),
            SecurityUsers.INSTANCE.getTEST_PASSWORD());

        log.warn("STARTED");
    }

    public static void main(final String[] args)
    {
        SpringApplication.run(Application.class, args);
    }
}
