package quebec.virtualite.backend.services.rest;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class WheelDTO
{
    @NotBlank
    private String brand;

    @NotBlank
    private String name;
}
