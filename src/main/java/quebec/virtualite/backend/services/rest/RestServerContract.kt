package quebec.virtualite.backend.services.rest

import javax.validation.Valid
import javax.validation.constraints.NotBlank

const val URL_ADD_WHEEL__PUT = "/wheels"
const val URL_DELETE_WHEEL = "/wheels/{name}"
const val URL_GET_WHEEL = "/wheels/{name}"
const val URL_GET_WHEELS = "/wheels"
const val URL_UPDATE_WHEEL__POST = "/wheels/{name}"

interface RestServerContract
{
    fun addWheel(@Valid wheel: WheelDTO)
    fun deleteWheel(@NotBlank name: String)
    fun getAllWheelDetails(): List<WheelDTO>
    fun getWheelDetails(@NotBlank name: String): WheelDTO
    fun updateWheel(@NotBlank name: String, @Valid updatedWheel: WheelDTO)
}
