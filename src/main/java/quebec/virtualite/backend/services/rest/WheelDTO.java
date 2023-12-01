package quebec.virtualite.backend.services.rest;

import lombok.With;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import javax.validation.constraints.NotBlank;

public record WheelDTO(
    @NotBlank
    @With
    String brand,

    @NotBlank
    @With
    String name
)
{
    public static WheelDTO toWheelDTO(WheelEntity entity)
    {
        return new WheelDTO(
            entity.getBrand(),
            entity.getName());
    }

    public WheelEntity toEntity(long id)
    {
        return new WheelEntity(id, brand, name);
    }
}
