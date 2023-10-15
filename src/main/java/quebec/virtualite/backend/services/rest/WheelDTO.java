package quebec.virtualite.backend.services.rest;

import lombok.Data;
import lombok.experimental.Accessors;

import java.beans.Transient;

import static quebec.virtualite.utils.CollectionUtils.isNullOrEmpty;

@Data
@Accessors(chain = true)
public class WheelDTO
{
    private String brand;
    private String name;

    @Transient
    public boolean isIncomplete()
    {
        return isNullOrEmpty(brand)
               || isNullOrEmpty(name);
    }
}
