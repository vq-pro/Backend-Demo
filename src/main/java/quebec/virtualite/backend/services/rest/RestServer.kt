package quebec.virtualite.backend.services.rest

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.util.ObjectUtils.isEmpty
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import quebec.virtualite.backend.services.domain.DomainService
import quebec.virtualite.backend.services.domain.entities.WheelEntity

@RestController
class RestServer(
    private val domainService: DomainService
)
{
    private val log = LoggerFactory.getLogger(this.javaClass)

    @PutMapping("/wheels")
    fun addWheel(@RequestBody dto: WheelDTO): ResponseEntity<Void>
    {
        if (!validate(dto))
            return ResponseEntity.badRequest().build()

        if (domainService.getWheelDetails(dto.name!!) != null)
            return ResponseEntity.status(CONFLICT).build()

        domainService.addWheel(convert(dto))
        return ResponseEntity.status(CREATED).build()
    }

    @DeleteMapping("/wheels/{name}")
    fun deleteWheel(@PathVariable name: String): ResponseEntity<Void>
    {
        domainService.deleteWheel(name)

        return ResponseEntity
            .ok()
            .build()
    }

    @GetMapping("/wheels")
    fun getAllWheelDetails(): ResponseEntity<Array<WheelDTO>>
    {
        return ResponseEntity
            .status(CREATED)
            .body(domainService.getAllWheelDetails()
                .map { w -> WheelDTO(w.brand, w.name) }
                .toTypedArray())
    }

    @GetMapping("/wheels/{name}")
    fun getWheelDetails(@PathVariable name: String?): ResponseEntity<WheelDTO>
    {
        if (name == null)
        {
            log.warn("name is not specified")
            return ResponseEntity.badRequest().build()
        }

        return domainService.getWheelDetails(name)
            ?.let { ResponseEntity.ok(WheelDTO(it.brand, it.name)) }
            ?: ResponseEntity.status(NOT_FOUND).build()
    }

    @PostMapping("/wheels/{name}")
    fun updateWheel(@PathVariable name: String, @RequestBody updatedWheel: WheelDTO): ResponseEntity<Void>
    {
        val wheel = domainService.getWheelDetails(name)
        domainService.saveWheel(WheelEntity(wheel!!.id, updatedWheel.brand!!, updatedWheel.name!!))

        return ResponseEntity
            .ok()
            .build()
    }

    private fun convert(dto: WheelDTO): WheelEntity
    {
        return WheelEntity(0, dto.brand!!, dto.name!!)
    }

    private fun validate(wheel: WheelDTO): Boolean
    {
        return !isEmpty(wheel.brand)
            && !isEmpty(wheel.name)
    }
}
