package quebec.virtualite.backend.services.domain;

import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import java.util.List;
import java.util.Optional;

public interface DomainService
{
    void addWheel(WheelEntity wheel);

    void deleteAll();

    Optional<WheelEntity> getWheelDetails(String wheelName);

    List<WheelEntity> getWheelsDetails();

    void saveWheel(WheelEntity wheel);

    void updateWheel(WheelEntity wheel);
}
