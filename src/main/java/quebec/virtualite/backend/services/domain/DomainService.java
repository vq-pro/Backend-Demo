package quebec.virtualite.backend.services.domain;

import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import java.util.List;
import java.util.Optional;

public interface DomainService
{
    void deleteAll();

    Optional<WheelEntity> getWheel(String wheelname);

    List<WheelEntity> getWheels();

    void saveWheel(WheelEntity wheel);
}
