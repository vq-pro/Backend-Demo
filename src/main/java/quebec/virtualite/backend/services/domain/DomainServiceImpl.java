package quebec.virtualite.backend.services.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import quebec.virtualite.backend.services.domain.database.WheelRepository;
import quebec.virtualite.backend.services.domain.entities.WheelAlreadyExistsException;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;
import quebec.virtualite.backend.services.domain.entities.WheelInvalidException;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
public class DomainServiceImpl implements DomainService
{
    private final WheelRepository wheelRepository;

    @Override
    public void addWheel(WheelEntity wheel)
    {
        if (!validate(wheel))
        {
            throw new WheelInvalidException();
        }

        if (wheelRepository.findByName(wheel.getName()).isPresent())
        {
            throw new WheelAlreadyExistsException();
        }

        wheelRepository.save(wheel);
    }

    @Override
    public void deleteAll()
    {
        wheelRepository.deleteAll();
    }

    @Override
    public void deleteWheel(WheelEntity wheel)
    {
        wheelRepository.delete(wheel);
    }

    @Override
    public Optional<WheelEntity> getWheel(String wheelName)
    {
        return wheelRepository.findByName(wheelName);
    }

    @Override
    public List<WheelEntity> getWheels()
    {
        return wheelRepository.findAll();
    }

    @Override
    public void saveWheel(WheelEntity wheel)
    {
        if (!validate(wheel))
        {
            throw new WheelInvalidException();
        }

        if (wheelRepository.findByName(wheel.getName()).isPresent())
        {
            throw new WheelAlreadyExistsException();
        }

        wheelRepository.save(wheel);
    }

    private boolean validate(WheelEntity wheel)
    {
        return !isNull(wheel)
               && isNotEmpty(wheel.getBrand())
               && isNotEmpty(wheel.getName());
    }
}
