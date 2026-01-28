package quebec.virtualite.backend.services.rest

import jakarta.validation.constraints.NotBlank
import quebec.virtualite.backend.services.domain.entities.CityEntity

data class CityDTO(

    @field:NotBlank
    val name: String?,

    @field:NotBlank
    val province: String?,
)
{
    constructor() : this(null, null)

    constructor(entity: CityEntity) : this(entity.name, entity.province)

    fun toEntity(id: Long): CityEntity = CityEntity(id, name!!, province!!)
}
