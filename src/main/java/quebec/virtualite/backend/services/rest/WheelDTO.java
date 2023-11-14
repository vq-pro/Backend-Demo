package quebec.virtualite.backend.services.rest;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class WheelDTO
{
    @NotBlank(message = "brand is not specified")
    private String brand;

    @NotBlank(message = "name is not specified")
    private String name;
}
