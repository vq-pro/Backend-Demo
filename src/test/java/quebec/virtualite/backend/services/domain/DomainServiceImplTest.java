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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static quebec.virtualite.utils.CollectionUtils.list;

@RunWith(MockitoJUnitRunner.class)
public class DomainServiceImplTest implements TestConstants
{
    @InjectMocks
    private DomainServiceImpl domainService;

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
        domainService.deleteAll();

        // Then
        verify(mockedWheelRepository).deleteAll();
    }

    @Test
    public void getWheel()
    {
        // Given
        WheelEntity wheel = mock(WheelEntity.class);
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.of(wheel));

        // When
        Optional<WheelEntity> response = domainService.getWheel(NAME);

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
        Optional<WheelEntity> wheel = domainService.getWheel(NAME);

        // Then
        assertThat(wheel).isEqualTo(Optional.empty());
    }

    @Test
    public void getWheels()
    {
        // Given
        given(mockedWheelRepository.findAll())
            .willReturn(list(WHEEL));

        // When
        List<WheelEntity> response = domainService.getWheels();

        // Then
        verify(mockedWheelRepository).findAll();

        assertThat(response).isEqualTo(list(WHEEL));
    }

    @Test
    public void saveWheel()
    {
        // When
        domainService.saveWheel(mockedWheel);

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
            domainService.saveWheel(mockedWheel));

        // Then
        assertThat(exception).isInstanceOf(WheelAlreadyExistsException.class);
    }
}
