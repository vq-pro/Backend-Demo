package quebec.virtualite.backend.services.rest

import javax.validation.Valid
import javax.validation.constraints.NotBlank

const val URL_ADD_CITY__PUT = "/cities"
const val URL_DELETE_CITY = "/cities/{name}"
const val URL_GET_CITY = "/cities/{name}"
const val URL_GET_CITIES = "/cities"
const val URL_UPDATE_CITY__POST = "/cities/{name}"

interface RestServerContract
{
    fun addCity(@Valid city: CityDTO)
    fun deleteCity(@NotBlank name: String)
    fun getCityDetails(@NotBlank name: String): CityDTO
    fun getCitiesDetails(): List<CityDTO>
    fun updateCity(@NotBlank name: String, @Valid updatedCity: CityDTO)
}
