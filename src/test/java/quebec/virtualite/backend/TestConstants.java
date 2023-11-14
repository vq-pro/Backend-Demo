package quebec.virtualite.backend;

import quebec.virtualite.backend.services.domain.entities.WheelEntity;
import quebec.virtualite.backend.services.rest.WheelDTO;

public interface TestConstants
{
    String BRAND = "Brand";
    String NAME = "Name";
    WheelEntity WHEEL = new WheelEntity()
        .setBrand(BRAND)
        .setName(NAME);
    WheelDTO WHEEL_DTO = new WheelDTO()
        .setBrand(BRAND)
        .setName(NAME);

    String EMPTY_BRAND = "";
    String EMPTY_NAME = "";

    String NULL_BRAND = null;
    String NULL_NAME = null;
}
