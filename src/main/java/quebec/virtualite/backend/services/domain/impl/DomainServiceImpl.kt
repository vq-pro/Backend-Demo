package quebec.virtualite.backend.services.domain.impl

import org.springframework.stereotype.Service
import quebec.virtualite.backend.services.domain.CityAlreadyExistsException
import quebec.virtualite.backend.services.domain.DomainService
import quebec.virtualite.backend.services.domain.entities.CityEntity
import quebec.virtualite.backend.services.domain.repositories.CityRepository
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
@Transactional
open class DomainServiceImpl(
    private val cityRepository: CityRepository

) : DomainService
{
    override fun addCity(city: CityEntity)
    {
        if (cityRepository.findByName(city.name) != null)
            throw CityAlreadyExistsException()

        cityRepository.save(city)
    }

    override fun deleteAll()
    {
        cityRepository.deleteAll()
    }

    override fun deleteCity(name: String)
    {
        cityRepository.deleteByName(name)
    }

    override fun getCityDetails(city: String): CityEntity?
    {
        return cityRepository.findByName(city)
    }

    override fun getCitiesDetails(): List<CityEntity>
    {
        return cityRepository.findAllByOrderByNameAscProvinceAsc()
    }

    override fun updateCity(city: CityEntity)
    {
        if (city.id == 0L)
        {
            throw EntityNotFoundException()
        }

        val existingCity = cityRepository.findByName(city.name)
        if (existingCity != null && existingCity.id != city.id)
        {
            throw CityAlreadyExistsException()
        }

        cityRepository.save(city)
    }
}
