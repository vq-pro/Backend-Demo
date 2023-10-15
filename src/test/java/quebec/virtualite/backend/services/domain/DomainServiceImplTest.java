package quebec.virtualite.backend.services.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import quebec.virtualite.backend.TestConstants;
import quebec.virtualite.backend.services.domain.database.WheelRepository;
import quebec.virtualite.backend.services.domain.entities.WheelAlreadyExistsException;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DomainServiceImplTest implements TestConstants
{
    private static final Long ID = 111L;

    @InjectMocks
    private DomainServiceImpl service;

    @Mock
    private WheelEntity mockedWheel;

    @Mock
    private WheelRepository mockedWheelRepository;

    @Before
    public void before()
    {
        given(mockedWheel.getName())
            .willReturn(NAME);
    }

    @Test
    public void deleteAll()
    {
        // When
        service.deleteAll();

        // Then
        verify(mockedWheelRepository).deleteAll();
    }

    @Test
    public void deleteWheel()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.of(WHEEL));

        // When
        service.deleteWheel(NAME);

        // Then
        verify(mockedWheelRepository).findByName(NAME);
        verify(mockedWheelRepository).delete(WHEEL);
    }

    @Test
    public void deleteWheel_whenNotFound_exception()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.empty());

        // When
        Throwable exception = catchThrowable(() ->
            service.deleteWheel(NAME));

        // Then
        assertThat(exception).isInstanceOf(EntityNotFoundException.class);

        verify(mockedWheelRepository, never()).delete(WHEEL);
    }

    @Test
    public void getWheelDetails()
    {
        // Given
        WheelEntity wheel = mock(WheelEntity.class);
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.of(wheel));

        // When
        Optional<WheelEntity> response = service.getWheelDetails(NAME);

        // Then
        verify(mockedWheelRepository).findByName(NAME);

        assertThat(response).isEqualTo(Optional.of(wheel));
    }

    @Test
    public void getWheelDetails_whenNotFound()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.empty());

        // When
        Optional<WheelEntity> wheel = service.getWheelDetails(NAME);

        // Then
        assertThat(wheel).isEqualTo(Optional.empty());
    }

    @Test
    public void saveWheel()
    {
        // When
        service.saveWheel(mockedWheel);

        // Then
        verify(mockedWheelRepository).findByName(NAME);
        verify(mockedWheelRepository).save(mockedWheel);
    }

    @Test
    public void saveWheel_whenAlreadyExists()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.of(new WheelEntity()));

        // When
        Throwable exception = catchThrowable(() ->
            service.saveWheel(mockedWheel));

        // Then
        assertThat(exception).isInstanceOf(WheelAlreadyExistsException.class);
    }

    @Test
    public void updateWheel()
    {
        // When
        service.updateWheel(WHEEL);

        // Then
        verify(mockedWheelRepository).save(WHEEL);
    }
}
