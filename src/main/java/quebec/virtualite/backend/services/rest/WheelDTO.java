package quebec.virtualite.backend.services.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
@With
@AllArgsConstructor
public class WheelDTO
{
    @NotBlank
    private String brand;

    @NotBlank
    private String name;
}
