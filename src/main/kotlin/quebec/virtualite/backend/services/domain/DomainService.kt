package quebec.virtualite.backend.services.domain

import quebec.virtualite.backend.services.domain.entities.CityEntity

interface DomainService
{
    fun addCity(city: CityEntity)
    fun deleteAll()
    fun deleteCity(name: String)
    fun getCityDetails(city: String): CityEntity?
    fun getCitiesDetails(): List<CityEntity>
    fun updateCity(city: CityEntity)
}