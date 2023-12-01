package quebec.virtualite.backend.services.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WheelDTO
{
    @NotBlank
    @With
    private String brand;

    @NotBlank
    @With
    private String name;

    public WheelDTO(WheelEntity entity)
    {
        this(entity.brand(), entity.name());
    }

    public WheelEntity toEntity(long id)
    {
        return new WheelEntity(id, brand, name);
    }
}
