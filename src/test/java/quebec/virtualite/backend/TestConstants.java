package quebec.virtualite.backend;

import quebec.virtualite.backend.services.domain.entities.WheelEntity;
import quebec.virtualite.backend.services.rest.WheelDTO;

public interface TestConstants
{
    String BRAND = "Brand";
    String NAME = "Name";
    String NULL_BRAND = null;
    String NULL_NAME = null;
    WheelEntity WHEEL = new WheelEntity()
        .setBrand(BRAND)
        .setName(NAME);
    WheelDTO WHEEL_DTO = new WheelDTO()
        .setBrand(BRAND)
        .setName(NAME);
}
