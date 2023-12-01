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
    WheelEntity WHEEL = new WheelEntity(0, BRAND, NAME);
    WheelEntity WHEEL_WITH_ID = new WheelEntity(ID, BRAND, NAME);
    WheelEntity WHEEL_WITH_ID2 = new WheelEntity(ID2, BRAND2, NAME);
    WheelDTO WHEEL_DTO = new WheelDTO()
        .setBrand(BRAND)
        .setName(NAME);
}
