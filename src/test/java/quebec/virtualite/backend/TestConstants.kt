package quebec.virtualite.backend

import quebec.virtualite.backend.services.domain.entities.WheelEntity

object TestConstants
{
    val ID = 111L
    val BRAND = "Brand"
    val NAME = "Wheel"
    val WHEEL = WheelEntity(ID, BRAND, NAME)
}
