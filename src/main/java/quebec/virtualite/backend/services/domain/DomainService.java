package quebec.virtualite.backend.services.domain;

import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import java.util.Optional;

public interface DomainService
{
    void deleteAll();

    Optional<WheelEntity> getWheelDetails(String wheelname);

    void saveWheel(String brand, String name);
}
