package quebec.virtualite.backend.services.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import quebec.virtualite.backend.services.domain.database.WheelRepository;
import quebec.virtualite.backend.services.domain.entities.WheelAlreadyExistsException;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DomainServiceImpl implements DomainService
{
    private final WheelRepository wheelRepository;

    @Override
    @Transactional
    public void addWheel(WheelEntity wheel)
    {
        if (wheelRepository.findByName(wheel.name()).isPresent())
        {
            throw new WheelAlreadyExistsException();
        }

        wheelRepository.save(wheel);
    }

    @Override
    @Transactional
    public void deleteAll()
    {
        wheelRepository.deleteAll();
    }

    @Override
    @Transactional
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
        return wheelRepository.findAllByOrderByBrandAscNameAsc();
    }

    @Override
    @Transactional
    public void updateWheel(WheelEntity wheel)
    {
        if (wheel.id() == 0)
        {
            throw new EntityNotFoundException();
        }

        Optional<WheelEntity> existingWheel = wheelRepository.findByName(wheel.name());
        if (existingWheel.isPresent()
            && existingWheel.get().id() != wheel.id())
        {
            throw new WheelAlreadyExistsException();
        }

        wheelRepository.save(wheel);
    }
}
