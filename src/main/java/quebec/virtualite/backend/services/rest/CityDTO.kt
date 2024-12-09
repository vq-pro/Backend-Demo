package quebec.virtualite.backend.services.rest

import quebec.virtualite.backend.services.domain.entities.CityEntity
import javax.validation.constraints.NotBlank

data class CityDTO(

    @field:NotBlank
    val name: String?,

    @field:NotBlank
    val province: String?,
)
{
    constructor(entity: CityEntity) : this(entity.name, entity.province)

    fun toEntity(id: Long): CityEntity = CityEntity(id, name!!, province!!)
}
