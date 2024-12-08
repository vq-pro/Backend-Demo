package quebec.virtualite.backend.services.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import quebec.virtualite.backend.services.domain.entities.CityEntity;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO
{
    @NotBlank
    @With
    private String name;

    @NotBlank
    @With
    private String province;

    public CityDTO(CityEntity entity)
    {
        this(entity.name(), entity.province());
    }

    public CityEntity toEntity(long id)
    {
        return new CityEntity(id, name, province);
    }
}
