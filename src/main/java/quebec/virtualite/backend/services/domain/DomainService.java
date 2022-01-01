package quebec.virtualite.backend.services.domain;

import quebec.virtualite.backend.services.domain.entities.GreetingEntity;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import java.util.List;

public interface DomainService
{
    void deleteAll();

    List<GreetingEntity> getGreetings();

    WheelEntity getWheelDetails(String wheelname);

    void recordGreeting(String name);

    void saveWheel(String brand, String name);
}
