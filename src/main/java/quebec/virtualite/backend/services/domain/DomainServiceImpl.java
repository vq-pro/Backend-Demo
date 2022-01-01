package quebec.virtualite.backend.services.domain;

import org.springframework.stereotype.Service;
import quebec.virtualite.backend.services.domain.database.GreetingRepository;
import quebec.virtualite.backend.services.domain.database.WheelRepository;
import quebec.virtualite.backend.services.domain.entities.GreetingEntity;
import quebec.virtualite.backend.services.domain.entities.WheelAlreadyExistsException;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DomainServiceImpl implements DomainService
{
    private final GreetingRepository greetingRepository;
    private final WheelRepository wheelRepository;

    public DomainServiceImpl(
        GreetingRepository greetingRepository,
        WheelRepository wheelRepository)
    {
        this.greetingRepository = greetingRepository;
        this.wheelRepository = wheelRepository;
    }

    @Override
    public void deleteAll()
    {
        greetingRepository.deleteAll();
        wheelRepository.deleteAll();
    }

    @Override
    public List<GreetingEntity> getGreetings()
    {
        return list(greetingRepository.findAll());
    }

    @Override
    public Optional<WheelEntity> getWheelDetails(String wheelName)
    {
        return wheelRepository.findByName(wheelName);
    }

    @Override
    public void recordGreeting(String name)
    {
        GreetingEntity greeting = greetingRepository.findByName(name)
            .orElse(new GreetingEntity()
                .setName(name));

        greetingRepository.save(greeting);
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

    private <T> List<T> list(Iterable<T> iterable)
    {
        List<T> list = new ArrayList<>();
        iterable.forEach(list::add);

        return list;
    }
}
