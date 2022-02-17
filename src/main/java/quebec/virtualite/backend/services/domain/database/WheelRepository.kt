package quebec.virtualite.backend.services.domain.database

import org.springframework.data.repository.CrudRepository
import quebec.virtualite.backend.services.domain.entities.WheelEntity

interface WheelRepository : CrudRepository<WheelEntity, Long>
{
    fun findByName(name: String): WheelEntity?
}
