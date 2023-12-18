package quebec.virtualite.backend.services.rest.impl

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import quebec.virtualite.backend.services.domain.DomainService
import quebec.virtualite.backend.services.domain.WheelAlreadyExistsException
import quebec.virtualite.backend.services.domain.entities.WheelEntity
import quebec.virtualite.backend.services.rest.RestServerContract
import quebec.virtualite.backend.services.rest.URL_ADD_WHEEL__PUT
import quebec.virtualite.backend.services.rest.URL_DELETE_WHEEL
import quebec.virtualite.backend.services.rest.URL_GET_WHEEL
import quebec.virtualite.backend.services.rest.URL_GET_WHEELS
import quebec.virtualite.backend.services.rest.URL_UPDATE_WHEEL__POST
import quebec.virtualite.backend.services.rest.WheelDTO
import quebec.virtualite.utils.CollectionUtils.transform

@RestController
@Validated
open class RestServer(
    private val domainService: DomainService

) : RestServerContract
{
    private val log = LoggerFactory.getLogger(this.javaClass)

    @PutMapping(URL_ADD_WHEEL__PUT)
    @ResponseStatus(CREATED)
    override fun addWheel(@RequestBody wheel: WheelDTO)
    {
        domainService.addWheel(wheel.toEntity(0))
    }

    @DeleteMapping(URL_DELETE_WHEEL)
    override fun deleteWheel(@PathVariable name: String)
    {
        getWheel(name)
        domainService.deleteWheel(name)
    }

    @GetMapping(URL_GET_WHEEL)
    override fun getWheelDetails(@PathVariable name: String): WheelDTO
    {
        return WheelDTO(getWheel(name))
    }

    @GetMapping(URL_GET_WHEELS)
    override fun getWheelsDetails()
        : List<WheelDTO>
    {
        return transform(domainService.getWheelsDetails())
        { WheelDTO(it) }
    }

    @PostMapping(URL_UPDATE_WHEEL__POST)
    override fun updateWheel(
        @PathVariable name: String, @RequestBody updatedWheel: WheelDTO
    )
    {
        domainService.updateWheel(
            updatedWheel.toEntity(getWheel(name).id)
        )
    }

    @ExceptionHandler(WheelAlreadyExistsException::class)
    internal fun exceptionHandler(e: Exception?): ResponseEntity<String>
    {
        return ResponseEntity(CONFLICT)
    }

    private fun getWheel(name: String): WheelEntity
    {
        return domainService.getWheelDetails(name)
            ?: throw ResponseStatusException(NOT_FOUND)
    }
}
