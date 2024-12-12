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
import quebec.virtualite.backend.services.domain.CityAlreadyExistsException
import quebec.virtualite.backend.services.domain.DomainService
import quebec.virtualite.backend.services.domain.entities.CityEntity
import quebec.virtualite.backend.services.rest.CityDTO
import quebec.virtualite.backend.services.rest.RestServerContract
import quebec.virtualite.backend.services.rest.URL_ADD_CITY__PUT
import quebec.virtualite.backend.services.rest.URL_DELETE_CITY
import quebec.virtualite.backend.services.rest.URL_GET_CITIES
import quebec.virtualite.backend.services.rest.URL_GET_CITY
import quebec.virtualite.backend.services.rest.URL_UPDATE_CITY__POST
import quebec.virtualite.utils.CollectionUtils.map

@RestController
@Validated
open class RestServer(
    private val domainService: DomainService

) : RestServerContract
{
    private val log = LoggerFactory.getLogger(this.javaClass)

    @PutMapping(URL_ADD_CITY__PUT)
    @ResponseStatus(CREATED)
    override fun addCity(@RequestBody city: CityDTO)
    {
        domainService.addCity(city.toEntity(0))
    }

    @DeleteMapping(URL_DELETE_CITY)
    override fun deleteCity(@PathVariable name: String)
    {
        getCity(name)
        domainService.deleteCity(name)
    }

    @GetMapping(URL_GET_CITY)
    override fun getCityDetails(@PathVariable name: String): CityDTO
    {
        return CityDTO(getCity(name))
    }

    @GetMapping(URL_GET_CITIES)
    override fun getCitiesDetails(): List<CityDTO>
    {
        return map(domainService.getCitiesDetails())
        { CityDTO(it) }
    }

    @PostMapping(URL_UPDATE_CITY__POST)
    override fun updateCity(
        @PathVariable name: String,
        @RequestBody updatedCity: CityDTO
    )
    {
        domainService.updateCity(
            updatedCity.toEntity(getCity(name).id)
        )
    }

    @ExceptionHandler(CityAlreadyExistsException::class)
    internal fun exceptionHandler(e: Exception?): ResponseEntity<String>
    {
        return ResponseEntity(CONFLICT)
    }

    private fun getCity(name: String): CityEntity
    {
        return domainService.getCityDetails(name)
            ?: throw ResponseStatusException(NOT_FOUND)
    }
}
