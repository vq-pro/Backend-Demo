package quebec.virtualite.backend.services.rest

import javax.validation.constraints.NotBlank

data class WheelDTO(
    @field:NotBlank
    val brand: String?,

    @field:NotBlank
    val name: String?
)
