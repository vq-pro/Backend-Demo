package quebec.virtualite.backend.services.domain

import quebec.virtualite.backend.services.domain.entities.WheelEntity

interface DomainService
{
    fun addWheel(wheel: WheelEntity)
    fun deleteAll()
    fun deleteWheel(name: String)
    fun getWheelDetails(wheelName: String): WheelEntity?
    fun getWheelsDetails(): List<WheelEntity>
    fun updateWheel(wheel: WheelEntity)
}