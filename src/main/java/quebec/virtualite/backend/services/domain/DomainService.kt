package quebec.virtualite.backend.services.domain

import quebec.virtualite.backend.services.domain.entities.WheelEntity

interface DomainService
{
    fun addWheel(wheel: WheelEntity?)
    fun deleteAll()
    fun deleteWheel(name: String)
    fun getAllWheelDetails(): List<WheelEntity>
    fun getWheelDetails(wheelName: String): WheelEntity?
    fun saveWheel(wheel: WheelEntity)
}