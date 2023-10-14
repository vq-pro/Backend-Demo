package quebec.virtualite.backend.services.domain;

import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import java.util.List;
import java.util.Optional;

public interface DomainService
{
    WheelEntity addWheel(WheelEntity wheel);

    void deleteAll();

    Optional<WheelEntity> getWheelDetails(String wheelname);

    List<WheelEntity> getWheelsDetails();

    void saveWheel(WheelEntity wheel);
}
