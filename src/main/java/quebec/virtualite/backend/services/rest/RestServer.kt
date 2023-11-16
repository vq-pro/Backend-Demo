package quebec.virtualite.backend.services.rest

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
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
@Validated
open class RestServer(
    private val domainService: DomainService
)
{
    private val log = LoggerFactory.getLogger(this.javaClass)

    @PutMapping("/wheels")
    @ResponseStatus(CREATED)
    open fun addWheel(@RequestBody @Valid dto: WheelDTO)
    {
        domainService.addWheel(convert(dto))
    }

    @DeleteMapping("/wheels/{name}")
    open fun deleteWheel(@PathVariable @NotBlank name: String)
    {
        getWheel(name)
        domainService.deleteWheel(name)
    }

    @GetMapping("/wheels")
    open fun getAllWheelDetails(): Array<WheelDTO>
    {
        return domainService.getAllWheelDetails()
            .map { wheel -> convert(wheel) }
            .toTypedArray()
    }

    @GetMapping("/wheels/{name}")
    open fun getWheelDetails(@PathVariable @NotBlank name: String): WheelDTO
    {
        return convert(getWheel(name))
    }

    @PostMapping("/wheels/{name}")
    open fun updateWheel(
        @PathVariable @NotBlank name: String,
        @RequestBody @Valid updatedWheel: WheelDTO
    )
    {
        domainService.updateWheel(
            convert(getWheel(name).id, updatedWheel)
        )
    }

    @ExceptionHandler(WheelAlreadyExistsException::class)
    internal fun exceptionHandler(e: Exception?): ResponseEntity<String>
    {
        return ResponseEntity(CONFLICT)
    }

    private fun convert(dto: WheelDTO): WheelEntity
    {
        return convert(0, dto)
    }

    private fun convert(id: Long, dto: WheelDTO): WheelEntity
    {
        return WheelEntity(
            id,
            dto.brand!!,
            dto.name!!,
        )
    }

    private fun convert(entity: WheelEntity): WheelDTO
    {
        return WheelDTO(
            entity.brand,
            entity.name,
        )
    }

    private fun getWheel(name: String): WheelEntity
    {
        return domainService.getWheelDetails(name)
            ?: throw ResponseStatusException(NOT_FOUND)
    }
}
