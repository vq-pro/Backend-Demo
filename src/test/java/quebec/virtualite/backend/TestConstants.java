package quebec.virtualite.backend;

import quebec.virtualite.backend.services.domain.entities.WheelEntity;
import quebec.virtualite.backend.services.rest.WheelDTO;

public interface TestConstants
{
    Long ID = 111L;
    Long ID2 = 112L;
    String BRAND = "Brand";
    String BRAND2 = "Brand2";
    String NAME = "Name";
    WheelEntity WHEEL = new WheelEntity()
        .setBrand(BRAND)
        .setName(NAME);
    WheelEntity WHEEL_WITH_ID = new WheelEntity()
        .setId(ID)
        .setBrand(BRAND)
        .setName(NAME);
    WheelEntity WHEEL_WITH_ID2 = new WheelEntity()
        .setId(ID2)
        .setBrand(BRAND2)
        .setName(NAME);
    WheelDTO WHEEL_DTO = new WheelDTO(BRAND, NAME);

    String EMPTY_BRAND = "";
    String EMPTY_NAME = "";

    String NULL_BRAND = null;
    String NULL_NAME = null;
}
