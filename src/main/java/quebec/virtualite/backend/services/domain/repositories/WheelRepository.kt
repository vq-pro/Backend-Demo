package quebec.virtualite.backend.services.domain.repositories

import org.springframework.data.repository.CrudRepository
import quebec.virtualite.backend.services.domain.entities.WheelEntity

interface WheelRepository : CrudRepository<WheelEntity, Long>
{
    fun deleteByName(name: String)
    override fun findAll(): List<WheelEntity>
    fun findByName(name: String): WheelEntity?
}
