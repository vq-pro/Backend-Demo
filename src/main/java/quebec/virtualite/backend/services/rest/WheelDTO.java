package quebec.virtualite.backend.services.rest;

import lombok.Data;
import lombok.experimental.Accessors;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class WheelDTO
{
    @NotBlank
    private String brand;

    @NotBlank
    private String name;

    public WheelDTO copy()
    {
        return new WheelDTO()
            .setBrand(brand)
            .setName(name);
    }

    public static WheelDTO toWheelDTO(WheelEntity entity)
    {
        return new WheelDTO()
            .setBrand(entity.getBrand())
            .setName(entity.getName());
    }

    public WheelEntity toEntity()
    {
        return new WheelEntity()
            .setBrand(brand)
            .setName(name);
    }
}
