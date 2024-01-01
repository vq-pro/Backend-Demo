package quebec.virtualite.backend.services.rest

import quebec.virtualite.backend.services.domain.entities.WheelEntity
import javax.validation.constraints.NotBlank

data class WheelDTO(
    @field:NotBlank
    val brand: String?,

    @field:NotBlank
    val name: String?
)
{
    constructor(entity: WheelEntity) : this(entity.brand, entity.name)

    fun toEntity(id: Long): WheelEntity = WheelEntity(id, brand!!, name!!)
}
