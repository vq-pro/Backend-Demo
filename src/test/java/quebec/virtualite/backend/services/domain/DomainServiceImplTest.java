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
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DomainServiceImplTest
{
    private static final long ID = 111L;
    private static final String BRAND = "brand";
    private static final String NAME = "name";
    private static final WheelEntity WHEEL = new WheelEntity(ID, BRAND, NAME);

    @InjectMocks
    private DomainServiceImpl domainService;

    @Mock
    private WheelRepository mockedWheelRepository;

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
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(WHEEL);

        // When
        Optional<WheelEntity> response = domainService.getWheelDetails(NAME);

        // Then
        verify(mockedWheelRepository).findByName(NAME);

        assertThat(response).isEqualTo(Optional.of(WHEEL));
    }

    @Test
    public void getWheelDetails_whenNotFound()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(null);

        // When
        Optional<WheelEntity> wheel = domainService.getWheelDetails(NAME);

        // Then
        assertThat(wheel).isEqualTo(Optional.empty());
    }

    @Test
    public void saveWheel()
    {
        // When
        domainService.saveWheel(WHEEL);

        // Then
        verify(mockedWheelRepository).findByName(NAME);
        verify(mockedWheelRepository).save(WHEEL);
    }

    @Test
    public void saveWheel_whenAlreadyExists()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(new WheelEntity());

        // When
        Throwable exception = catchThrowable(() ->
            domainService.saveWheel(WHEEL));

        // Then
        assertThat(exception).isInstanceOf(WheelAlreadyExistsException.class);
    }
}
