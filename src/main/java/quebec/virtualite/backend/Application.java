package quebec.virtualite.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import quebec.virtualite.backend.security.SecurityUserManager;

import javax.annotation.PostConstruct;

import static quebec.virtualite.backend.security.SecurityUsers.TEST_PASSWORD;
import static quebec.virtualite.backend.security.SecurityUsers.TEST_USER;

@SpringBootApplication(scanBasePackages = {"quebec.virtualite.*"})
@Slf4j
public class Application
{
    @Autowired
    private SecurityUserManager userManager;

    static
    {
        log.warn("STARTING...");
    }

    public static void main(final String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void init()
    {
        userManager.defineUser(TEST_USER, TEST_PASSWORD);

        log.warn("STARTED");
    }
}
