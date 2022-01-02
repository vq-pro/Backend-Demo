package quebec.virtualite.backend.services.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import quebec.virtualite.backend.services.domain.database.WheelRepository;
import quebec.virtualite.backend.services.domain.entities.WheelAlreadyExistsException;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DomainServiceImplTest
{
    private static final String BRAND = "brand";
    private static final String NAME = "name";

    @Mock
    private WheelRepository mockedWheelRepository;

    @InjectMocks
    private DomainServiceImpl domainService;

    @Test
    public void deleteAll()
    {
        // When
        domainService.deleteAll();

        // Then
        verify(mockedWheelRepository).deleteAll();
    }

    @Test
    public void getWheelDetails()
    {
        // Given
        WheelEntity wheel = mock(WheelEntity.class);
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.of(wheel));

        // When
        Optional<WheelEntity> response = domainService.getWheelDetails(NAME);

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
        Optional<WheelEntity> wheel = domainService.getWheelDetails(NAME);

        // Then
        assertThat(wheel).isEqualTo(Optional.empty());
    }

    @Test
    public void saveWheel()
    {
        // When
        domainService.saveWheel(BRAND, NAME);

        // Then
        verify(mockedWheelRepository).findByName(NAME);
        verify(mockedWheelRepository).save(
            new WheelEntity()
                .setBrand(BRAND)
                .setName(NAME));
    }

    @Test
    public void saveWheel_whenAlreadyExists()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(Optional.of(new WheelEntity()));

        // When
        Throwable exception = catchThrowable(() -> domainService.saveWheel(BRAND, NAME));

        // Then
        assertThat(exception).isInstanceOf(WheelAlreadyExistsException.class);
    }
}
