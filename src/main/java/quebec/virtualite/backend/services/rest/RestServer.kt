package quebec.virtualite.backend.services.rest

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.util.ObjectUtils.isEmpty
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import quebec.virtualite.backend.services.domain.DomainService
import quebec.virtualite.backend.services.domain.entities.WheelEntity
import java.util.Objects.isNull

@RestController
class RestServer(
    private val domainService: DomainService
)
{
    private val log = LoggerFactory.getLogger(this.javaClass)

    @PutMapping("/wheels")
    @ResponseStatus(CREATED)
    fun addWheel(@RequestBody dto: WheelDTO?)
    {
        validateWheel(dto)

        if (domainService.getWheelDetails(dto!!.name!!) != null)
            throw ResponseStatusException(CONFLICT)

        domainService.addWheel(convert(dto))
    }

    @DeleteMapping("/wheels/{name}")
    fun deleteWheel(@PathVariable name: String?)
    {
        validateName(name)

        getWheel(name!!)
        domainService.deleteWheel(name)
    }

    @GetMapping("/wheels")
    fun getAllWheelDetails(): Array<WheelDTO>
    {
        return domainService.getAllWheelDetails()
            .map { w -> WheelDTO(w.brand, w.name) }
            .toTypedArray()
    }

    @GetMapping("/wheels/{name}")
    fun getWheelDetails(@PathVariable name: String?): WheelDTO
    {
        validateName(name)

        return convert(getWheel(name!!))
    }

    @PostMapping("/wheels/{name}")
    fun updateWheel(@PathVariable name: String?, @RequestBody updatedWheel: WheelDTO?)
    {
        validateName(name)
        validateWheel(updatedWheel)

        domainService.saveWheel(
            WheelEntity(
                getWheel(name!!).id,
                updatedWheel!!.brand!!,
                updatedWheel.name!!
            )
        )
    }

    private fun convert(dto: WheelDTO): WheelEntity
    {
        return WheelEntity(0, dto.brand!!, dto.name!!)
    }

    private fun convert(entity: WheelEntity): WheelDTO
    {
        return WheelDTO(entity.brand, entity.name)
    }

    private fun getWheel(name: String): WheelEntity
    {
        return domainService.getWheelDetails(name)
            ?: throw ResponseStatusException(NOT_FOUND)
    }

    private fun validateName(name: String?)
    {
        if (isNull(name))
        {
            log.warn("name is not specified")
            throw ResponseStatusException(BAD_REQUEST)
        }
    }

    private fun validateWheel(wheel: WheelDTO?)
    {
        if (isNull(wheel)
            || isEmpty(wheel!!.brand)
            || isEmpty(wheel.name)
        )
            throw ResponseStatusException(BAD_REQUEST)
    }
}
