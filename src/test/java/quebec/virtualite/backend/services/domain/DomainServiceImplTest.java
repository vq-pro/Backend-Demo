package quebec.virtualite.backend.services.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import quebec.virtualite.backend.services.domain.database.WheelRepository;
import quebec.virtualite.backend.services.domain.entities.WheelAlreadyExistsException;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;
import quebec.virtualite.backend.services.domain.entities.WheelInvalidException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static quebec.virtualite.backend.TestConstants.BRAND;
import static quebec.virtualite.backend.TestConstants.NAME;
import static quebec.virtualite.backend.TestConstants.NULL_BRAND;
import static quebec.virtualite.backend.TestConstants.NULL_NAME;
import static quebec.virtualite.backend.TestConstants.WHEEL;
import static quebec.virtualite.utils.CollectionUtils.list;

@RunWith(MockitoJUnitRunner.class)
public class DomainServiceImplTest
{
    @InjectMocks
    private DomainServiceImpl service;

    @Mock
    private WheelRepository mockedWheelRepository;

    @Test
    public void addWheel()
    {
        // When
        service.addWheel(WHEEL);

        // Then
        verify(mockedWheelRepository).save(WHEEL);
    }

    @Test
    public void deleteWheel()
    {
        // When
        service.deleteWheel(WHEEL);

        // Then
        verify(mockedWheelRepository).delete(WHEEL);
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
        assertThat(exception).isInstanceOf(WheelAlreadyExistsException.class);

        verify(mockedWheelRepository).findByName(NAME);
        verify(mockedWheelRepository, never()).save(WHEEL);
    }

    @Test
    public void addWheel_withNullField_exception()
    {
        addWheel_withNullField(NULL_BRAND, NAME);
        addWheel_withNullField(BRAND, NULL_NAME);
        addWheel_withNullField(NULL_BRAND, NULL_NAME);
    }

    @Test
    public void addWheel_withNullPayload_exception()
    {
        // When
        Throwable exception = catchThrowable(() ->
            service.addWheel(null));

        // Then
        assertThat(exception).isInstanceOf(WheelInvalidException.class);

        verify(mockedWheelRepository, never()).save(WHEEL);
    }

    @Test
    public void getWheel()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.of(WHEEL));

        // When
        Optional<WheelEntity> response = service.getWheel(NAME);

        // Then
        assertThat(response).isEqualTo(Optional.of(WHEEL));

        verify(mockedWheelRepository).findByName(NAME);
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
    public void getWheels()
    {
        // Given
        given(mockedWheelRepository.findAll())
            .willReturn(list(WHEEL));

        // When
        List<WheelEntity> response = service.getWheels();

        // Then
        assertThat(response).isEqualTo(list(WHEEL));

        verify(mockedWheelRepository).findAll();
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
    public void saveWheel()
    {
        // When
        service.saveWheel(WHEEL);

        // Then
        verify(mockedWheelRepository).findByName(NAME);
        verify(mockedWheelRepository).save(WHEEL);
    }

    @Test
    public void saveWheel_whenAlreadyExists()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.of(WHEEL));

        // When
        Throwable exception = catchThrowable(() ->
            service.saveWheel(WHEEL));

        // Then
        assertThat(exception).isInstanceOf(WheelAlreadyExistsException.class);
    }

    @Test
    public void saveWheel_withNullField_exception()
    {
        saveWheel_withNullField(NULL_BRAND, NAME);
        saveWheel_withNullField(BRAND, NULL_NAME);
        saveWheel_withNullField(NULL_BRAND, NULL_NAME);
    }

    @Test
    public void saveWheel_withNullPayload_exception()
    {
        // When
        Throwable exception = catchThrowable(() ->
            service.saveWheel(null));

        // Then
        assertThat(exception).isInstanceOf(WheelInvalidException.class);

        verify(mockedWheelRepository, never()).save(WHEEL);
    }

    private void addWheel_withNullField(String brand, String name)
    {
        // Given
        WheelEntity wheel = new WheelEntity()
            .setBrand(brand)
            .setName(name);

        // When
        Throwable exception = catchThrowable(() ->
            service.addWheel(wheel));

        // Then
        assertThat(exception).isInstanceOf(WheelInvalidException.class);

        verify(mockedWheelRepository, never()).save(wheel);
    }

    private void saveWheel_withNullField(String brand, String name)
    {
        // Given
        WheelEntity wheel = new WheelEntity()
            .setBrand(brand)
            .setName(name);

        // When
        Throwable exception = catchThrowable(() ->
            service.saveWheel(wheel));

        // Then
        assertThat(exception).isInstanceOf(WheelInvalidException.class);

        verify(mockedWheelRepository, never()).save(wheel);
    }
}
