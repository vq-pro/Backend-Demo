package quebec.virtualite.backend.services.rest;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WheelResponse
{
    private String brand;
    private String name;
}
