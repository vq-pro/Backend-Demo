package quebec.virtualite.backend.services.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import quebec.virtualite.backend.services.domain.database.WheelRepository;
import quebec.virtualite.backend.services.domain.entities.WheelAlreadyExistsException;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DomainServiceImpl implements DomainService
{
    private final WheelRepository wheelRepository;

    @Override
    public void deleteAll()
    {
        wheelRepository.deleteAll();
    }

    @Override
    public Optional<WheelEntity> getWheelDetails(String wheelName)
    {
        return Optional.ofNullable(wheelRepository.findByName(wheelName));
    }

    @Override
    public void saveWheel(WheelEntity wheel)
    {
        if (wheelRepository.findByName(wheel.getName()) != null)
        {
            throw new WheelAlreadyExistsException();
        }

        wheelRepository.save(wheel);
    }
}
