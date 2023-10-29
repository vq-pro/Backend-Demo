package quebec.virtualite.backend

import quebec.virtualite.backend.services.domain.entities.WheelEntity

object TestConstants
{
    val ID = 111L
    val ID2 = 112L
    val BRAND = "Brand"
    val BRAND2 = "Brand2"
    val NAME = "Wheel"
    val NAME2 = "Wheel2"
    val WHEEL = WheelEntity(ID, BRAND, NAME)
    val WHEEL2 = WheelEntity(ID2, BRAND2, NAME2)
}
