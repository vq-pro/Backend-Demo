package quebec.virtualite.backend.services.rest

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank

const val URL_ADD_CITY__PUT = "/cities"
const val URL_DELETE_CITY = "/cities/{name}"
const val URL_DELETE_CITY_WITHOUT_NAME = "/cities/"
const val URL_GET_CITY = "/cities/{name}"
const val URL_GET_CITIES = "/cities"
const val URL_UPDATE_CITY__POST = "/cities/{name}"
const val URL_UPDATE_CITY__POST_WITHOUT_NAME = "/cities/"

interface RestServerContract
{
    fun addCity(@Valid city: CityDTO)
    fun deleteCity(@NotBlank name: String)
    fun getCityDetails(@NotBlank name: String): CityDTO
    fun getCitiesDetails(): List<CityDTO>
    fun updateCity(@NotBlank name: String, @Valid updatedCity: CityDTO)
}
