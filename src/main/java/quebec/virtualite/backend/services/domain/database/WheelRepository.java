package quebec.virtualite.backend.services.domain.database;

import org.springframework.data.repository.CrudRepository;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import java.util.List;
import java.util.Optional;

public interface WheelRepository extends CrudRepository<WheelEntity, Long>
{
    List<WheelEntity> findAll();

    Optional<WheelEntity> findByName(String name);
}
