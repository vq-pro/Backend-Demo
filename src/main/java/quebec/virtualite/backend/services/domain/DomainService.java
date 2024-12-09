package quebec.virtualite.backend.services.domain;

import quebec.virtualite.backend.services.domain.entities.CityEntity;

import java.util.List;
import java.util.Optional;

public interface DomainService
{
    void addCity(CityEntity city);

    void deleteAll();

    void deleteCity(CityEntity city);

    List<CityEntity> getCities();

    Optional<CityEntity> getCity(String cityName);

    void updateCity(CityEntity city);
}
