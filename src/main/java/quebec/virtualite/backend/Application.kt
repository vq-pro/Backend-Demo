package quebec.virtualite.backend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import quebec.virtualite.backend.Application
import quebec.virtualite.backend.security.SecurityUserManager
import quebec.virtualite.backend.security.SecurityUsers.TEST_PASSWORD
import quebec.virtualite.backend.security.SecurityUsers.TEST_USER

@SpringBootApplication(scanBasePackages = ["quebec.virtualite.*"])
open class Application(userManager: SecurityUserManager)
{
    init
    {
        userManager.defineUser(TEST_USER, TEST_PASSWORD)

        // FIXME-0 Implement logging
//        log.warn("STARTED")
    }

    companion object
    {
        init
        {
//            Application.log.warn("STARTING...")
        }

        @JvmStatic
        fun main(args: Array<String>)
        {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}
