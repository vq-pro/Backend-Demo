package quebec.virtualite.backend.services.domain;

import quebec.virtualite.backend.services.domain.entities.GreetingEntity;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import java.util.List;
import java.util.Optional;

public interface DomainService
{
    void deleteAll();

    List<GreetingEntity> getGreetings();

    Optional<WheelEntity> getWheelDetails(String wheelname);

    void recordGreeting(String name);

    void saveWheel(String brand, String name);
}
