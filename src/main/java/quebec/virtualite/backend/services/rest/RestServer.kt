package quebec.virtualite.backend.services.rest

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import quebec.virtualite.backend.services.domain.DomainService

@RestController
class RestServer(
    private val domainService: DomainService
)
{
    private val log = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/wheels/{name}")
    fun getWheelDetails(@PathVariable name: String?): ResponseEntity<WheelResponse>
    {
        if (name == null)
        {
            log.warn("name is not specified")
            return ResponseEntity.badRequest().build()
        }

        return domainService.getWheelDetails(name)
            ?.let {
                ResponseEntity.ok().body(
                    WheelResponse(it.brand, it.name)
                )
            }
            ?: ResponseEntity.status(NOT_FOUND).build()
    }
}
