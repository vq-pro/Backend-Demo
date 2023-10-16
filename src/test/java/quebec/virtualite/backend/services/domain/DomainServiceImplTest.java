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
import quebec.virtualite.backend.services.domain.entities.WheelInvalidException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static quebec.virtualite.utils.CollectionUtils.list;

@RunWith(MockitoJUnitRunner.class)
public class DomainServiceImplTest implements TestConstants
{
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
    public void addWheel()
    {
        // When
        service.addWheel(WHEEL);

        // Then
        verify(mockedWheelRepository).save(WHEEL);
    }

    @Test
    public void addWheel_whenDuplicate_exception()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.of(WHEEL));

        // When
        Throwable exception = catchThrowable(() ->
            service.addWheel(WHEEL));

        // Then
        verify(mockedWheelRepository).findByName(NAME);
        verify(mockedWheelRepository, never()).save(WHEEL);

        assertThat(exception).isInstanceOf(WheelAlreadyExistsException.class);
    }

    @Test
    public void addWheel_withNullField_exception()
    {
        addWheel_withNullField(NULL_BRAND, NAME);
        addWheel_withNullField(BRAND, NULL_NAME);
        addWheel_withNullField(NULL_BRAND, NULL_NAME);
    }

    private void addWheel_withNullField(String brand, String name)
    {
        // When
        Throwable exception = catchThrowable(() ->
            service.addWheel(new WheelEntity()
                .setBrand(brand)
                .setName(name)));

        // Then
        verify(mockedWheelRepository, never()).save(WHEEL);

        assertThat(exception).isInstanceOf(WheelInvalidException.class);
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
    public void getWheel()
    {
        // Given
        WheelEntity wheel = mock(WheelEntity.class);
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.of(wheel));

        // When
        Optional<WheelEntity> response = service.getWheel(NAME);

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
        Optional<WheelEntity> wheel = service.getWheel(NAME);

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
        List<WheelEntity> response = service.getWheels();

        // Then
        verify(mockedWheelRepository).findAll();

        assertThat(response).isEqualTo(list(WHEEL));
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
}
