package quebec.virtualite.backend

import quebec.virtualite.backend.services.domain.entities.CityEntity
import quebec.virtualite.backend.services.rest.CityDTO

object TestConstants
{
    val ID = 111L
    val ID2 = 112L
    val NAME = "Name"
    val NAME2 = "Name2"
    val PROVINCE = "Province"
    val PROVINCE2 = "Province2"

    val CITY = CityEntity(0, NAME, PROVINCE)
    val CITY2 = CityEntity(0, NAME2, PROVINCE2)
    val CITY_DTO = CityDTO(NAME, PROVINCE)
    val CITY_DTO2 = CityDTO(NAME2, PROVINCE2)
    val CITY_WITH_ID = CityEntity(ID, NAME, PROVINCE)
    val CITY_WITH_ID2 = CityEntity(ID2, NAME2, PROVINCE2)

    val BAD_CITY_DTO = CityDTO("", "")
}
