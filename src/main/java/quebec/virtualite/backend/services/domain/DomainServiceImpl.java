package quebec.virtualite.backend.services.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import quebec.virtualite.backend.services.domain.database.CityRepository;
import quebec.virtualite.backend.services.domain.entities.CityAlreadyExistsException;
import quebec.virtualite.backend.services.domain.entities.CityEntity;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DomainServiceImpl implements DomainService
{
    private final CityRepository cityRepository;

    @Override
    public void addCity(CityEntity city)
    {
        if (cityRepository.findByName(city.name()).isPresent())
        {
            throw new CityAlreadyExistsException();
        }

        cityRepository.save(city);
    }

    @Override
    public void deleteAll()
    {
        cityRepository.deleteAll();
    }

    @Override
    public void deleteCity(CityEntity city)
    {
        cityRepository.delete(city);
    }

    @Override
    public List<CityEntity> getCities()
    {
        return cityRepository.findAllByOrderByNameAscProvinceAsc();
    }

    @Override
    public Optional<CityEntity> getCity(String cityName)
    {
        return cityRepository.findByName(cityName);
    }

    @Override
    public void updateCity(CityEntity city)
    {
        if (city.id() == 0)
        {
            throw new EntityNotFoundException();
        }

        Optional<CityEntity> existingCity = cityRepository.findByName(city.name());
        if (existingCity.isPresent()
            && existingCity.get().id() != city.id())
        {
            throw new CityAlreadyExistsException();
        }

        cityRepository.save(city);
    }
}
