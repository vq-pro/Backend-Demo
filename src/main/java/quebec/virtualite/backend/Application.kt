package quebec.virtualite.backend

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import quebec.virtualite.backend.security.SecurityUserManager
import quebec.virtualite.backend.security.SecurityUsers.TEST_PASSWORD
import quebec.virtualite.backend.security.SecurityUsers.TEST_USER

@SpringBootApplication(scanBasePackages = ["quebec.virtualite.*"])
open class Application(userManager: SecurityUserManager)
{
    companion object
    {
        private val log = LoggerFactory.getLogger(this::class.java)

        init
        {
            log.warn("STARTING...")
        }
    }

    init
    {
        userManager.defineUser(TEST_USER, TEST_PASSWORD)
        log.warn("STARTED")
    }

    fun main(args: Array<String>)
    {
        runApplication<Application>(*args)
    }
}
