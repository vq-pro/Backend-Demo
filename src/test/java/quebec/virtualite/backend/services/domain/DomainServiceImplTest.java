package quebec.virtualite.backend.services.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import quebec.virtualite.backend.services.domain.database.GreetingRepository;
import quebec.virtualite.backend.services.domain.entities.GreetingEntity;

import java.util.List;

import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DomainServiceImplTest
{
    private static final String NAME = "name";

    @Mock
    private GreetingRepository mockedGreetingRepository;

    @InjectMocks
    private DomainServiceImpl domainService;

    @Test
    public void deleteGreetings()
    {
        // When
        domainService.deleteGreetings();

        // Then
        verify(mockedGreetingRepository).deleteAll();
    }

    @Test
    public void getGreetings()
    {
        // When
        List<GreetingEntity> greetings = domainService.getGreetings();

        // Then
        verify(mockedGreetingRepository).findAll();

        assertThat(greetings, not(nullValue()));
    }

    @Test
    public void recordGreeting()
    {
        // When
        domainService.recordGreeting(NAME);

        // Then
        verify(mockedGreetingRepository).findByName(NAME);
        verify(mockedGreetingRepository).save(
            new GreetingEntity()
                .setName(NAME));
    }
}
