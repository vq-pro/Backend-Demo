package quebec.virtualite.backend;

import quebec.virtualite.backend.services.domain.entities.WheelEntity;

public interface TestConstants
{
    String BRAND = "Brand";
    String NAME = "Name";
    WheelEntity WHEEL = new WheelEntity()
        .setBrand(BRAND)
        .setName(NAME);
}
