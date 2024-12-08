package quebec.virtualite.backend.services.domain.database;

import org.springframework.data.repository.CrudRepository;
import quebec.virtualite.backend.services.domain.entities.CityEntity;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends CrudRepository<CityEntity, Long>
{
    List<CityEntity> findAllByOrderByNameAscProvinceAsc();

    Optional<CityEntity> findByName(String name);
}
