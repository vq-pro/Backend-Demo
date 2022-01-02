package quebec.virtualite.backend.services.domain;

import org.springframework.stereotype.Service;
import quebec.virtualite.backend.services.domain.database.WheelRepository;
import quebec.virtualite.backend.services.domain.entities.WheelAlreadyExistsException;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import java.util.Optional;

@Service
public class DomainServiceImpl implements DomainService
{
    private final WheelRepository wheelRepository;

    public DomainServiceImpl(WheelRepository wheelRepository)
    {
        this.wheelRepository = wheelRepository;
    }

    @Override
    public void deleteAll()
    {
        wheelRepository.deleteAll();
    }

    @Override
    public Optional<WheelEntity> getWheelDetails(String wheelName)
    {
        return wheelRepository.findByName(wheelName);
    }

    @Override
    public void saveWheel(String brand, String name)
    {
        wheelRepository.findByName(name).ifPresentOrElse(
            wheel ->
            {
                throw new WheelAlreadyExistsException();
            },
            () ->
                wheelRepository.save(
                    new WheelEntity()
                        .setBrand(brand)
                        .setName(name)));
    }
}
