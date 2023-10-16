package quebec.virtualite.backend;

import quebec.virtualite.backend.services.domain.entities.WheelEntity;

public interface TestConstants
{
    String BRAND = "Brand";
    String NAME = "Wheel";
    String NULL_BRAND = null;
    String NULL_NAME = null;
    WheelEntity WHEEL = new WheelEntity()
        .setBrand(BRAND)
        .setName(NAME);
}
