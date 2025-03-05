package quebec.virtualite.backend.services.rest;

import jakarta.validation.constraints.NotBlank;
import lombok.With;
import quebec.virtualite.backend.services.domain.entities.CityEntity;

public record CityDTO(@NotBlank @With String name, @NotBlank @With String province)
{
    public CityDTO(CityEntity entity)
    {
        this(entity.name(), entity.province());
    }

    public CityEntity toEntity(long id)
    {
        return new CityEntity(id, name, province);
    }
}
