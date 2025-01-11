package quebec.virtualite.backend.services.domain.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import quebec.virtualite.backend.services.domain.entities.CityEntity

@Component
interface CityRepository : CrudRepository<CityEntity, Long>
{
    fun deleteByName(name: String)
    fun findAllByOrderByNameAscProvinceAsc(): List<CityEntity>
    fun findByName(name: String): CityEntity?
}
