package quebec.virtualite.backend.services.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.virtualite.backend.services.domain.database.WheelRepository;
import quebec.virtualite.backend.services.domain.entities.WheelAlreadyExistsException;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static quebec.virtualite.backend.TestConstants.NAME;
import static quebec.virtualite.backend.TestConstants.WHEEL;
import static quebec.virtualite.backend.TestConstants.WHEEL_WITH_ID;
import static quebec.virtualite.backend.TestConstants.WHEEL_WITH_ID2;

@ExtendWith(MockitoExtension.class)
class DomainServiceImplTest
{
    @InjectMocks
    private DomainServiceImpl domain;

    @Mock
    private WheelRepository mockedWheelRepository;

    @Test
    void addWheel()
    {
        // When
        domain.addWheel(WHEEL);

        // Then
        verify(mockedWheelRepository).save(WHEEL);
    }

    @Test
    void addWheel_whenDuplicate_exception()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.of(WHEEL));

        // When
        Throwable exception = catchThrowable(() ->
            domain.addWheel(WHEEL));

        // Then
        verify(mockedWheelRepository).findByName(NAME);
        verify(mockedWheelRepository, never()).save(WHEEL);

        assertThat(exception).isInstanceOf(WheelAlreadyExistsException.class);
    }

    @Test
    void deleteAll()
    {
        // When
        domain.deleteAll();

        // Then
        verify(mockedWheelRepository).deleteAll();
    }

    @Test
    void deleteWheel()
    {
        // When
        domain.deleteWheel(WHEEL);

        // Then
        verify(mockedWheelRepository).delete(WHEEL);
    }

    @Test
    void getWheel()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.of(WHEEL));

        // When
        Optional<WheelEntity> response = domain.getWheel(NAME);

        // Then
        verify(mockedWheelRepository).findByName(NAME);

        assertThat(response).isEqualTo(Optional.of(WHEEL));
    }

    @Test
    void getWheelDetails_whenNotFound()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.empty());

        // When
        Optional<WheelEntity> wheel = domain.getWheel(NAME);

        // Then
        assertThat(wheel).isEqualTo(Optional.empty());
    }

    @Test
    void getWheels()
    {
        // Given
        given(mockedWheelRepository.findAllByOrderByBrandAscNameAsc())
            .willReturn(List.of(WHEEL));

        // When
        List<WheelEntity> response = domain.getWheels();

        // Then
        verify(mockedWheelRepository).findAllByOrderByBrandAscNameAsc();

        assertThat(response).isEqualTo(List.of(WHEEL));
    }

    @Test
    void updateWheel()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.of(WHEEL_WITH_ID));

        // When
        domain.updateWheel(WHEEL_WITH_ID);

        // Then
        verify(mockedWheelRepository).findByName(NAME);
        verify(mockedWheelRepository).save(WHEEL_WITH_ID);
    }

    @Test
    void updateWheel_whenDuplicate_exception()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.of(WHEEL_WITH_ID2));

        // When
        Throwable exception = catchThrowable(() ->
            domain.updateWheel(WHEEL_WITH_ID));

        // Then
        verify(mockedWheelRepository, never()).save(any());

        assertThat(exception).isInstanceOf(WheelAlreadyExistsException.class);
    }

    @Test
    void updateWheel_withNoId_exception()
    {
        // When
        Throwable exception = catchThrowable(() ->
            domain.updateWheel(new WheelEntity()));

        // Then
        verify(mockedWheelRepository, never()).save(any());
        assertThat(exception).isInstanceOf(EntityNotFoundException.class);
    }
}
