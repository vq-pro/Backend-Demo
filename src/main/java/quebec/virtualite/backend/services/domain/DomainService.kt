package quebec.virtualite.backend.services.domain

import quebec.virtualite.backend.services.domain.entities.WheelEntity

interface DomainService
{
    fun deleteAll()
    fun getWheelDetails(wheelName: String): WheelEntity?
    fun saveWheel(wheel: WheelEntity)
}