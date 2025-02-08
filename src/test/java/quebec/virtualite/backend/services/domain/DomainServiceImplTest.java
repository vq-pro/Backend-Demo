package quebec.virtualite.backend.services.domain;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.virtualite.backend.services.domain.database.CityRepository;
import quebec.virtualite.backend.services.domain.entities.CityAlreadyExistsException;
import quebec.virtualite.backend.services.domain.entities.CityEntity;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static quebec.virtualite.backend.TestConstants.CITY;
import static quebec.virtualite.backend.TestConstants.CITY_WITH_ID;
import static quebec.virtualite.backend.TestConstants.CITY_WITH_ID2;
import static quebec.virtualite.backend.TestConstants.NAME;

@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
class DomainServiceImplTest
{
    @InjectMocks
    private DomainServiceImpl domain;

    @Mock
    private CityRepository mockedCityRepository;

    @Test
    void addCity()
    {
        // When
        domain.addCity(CITY);

        // Then
        verify(mockedCityRepository).save(CITY);
    }

    @Test
    void addCity_whenDuplicate_exception()
    {
        // Given
        given(mockedCityRepository.findByName(NAME))
            .willReturn(Optional.of(CITY));

        // When
        Throwable exception = catchThrowable(() ->
            domain.addCity(CITY));

        // Then
        verify(mockedCityRepository).findByName(NAME);
        verify(mockedCityRepository, never()).save(CITY);

        assertThat(exception).isInstanceOf(CityAlreadyExistsException.class);
    }

    @Test
    void deleteAll()
    {
        // When
        domain.deleteAll();

        // Then
        verify(mockedCityRepository).deleteAll();
    }

    @Test
    void deleteCity()
    {
        // When
        domain.deleteCity(CITY);

        // Then
        verify(mockedCityRepository).delete(CITY);
    }

    @Test
    void getCities()
    {
        // Given
        given(mockedCityRepository.findAllByOrderByNameAscProvinceAsc())
            .willReturn(List.of(CITY));

        // When
        List<CityEntity> response = domain.getCities();

        // Then
        verify(mockedCityRepository).findAllByOrderByNameAscProvinceAsc();

        assertThat(response).isEqualTo(List.of(CITY));
    }

    @Test
    void getCity()
    {
        // Given
        given(mockedCityRepository.findByName(NAME))
            .willReturn(Optional.of(CITY));

        // When
        Optional<CityEntity> response = domain.getCity(NAME);

        // Then
        verify(mockedCityRepository).findByName(NAME);

        assertThat(response).isEqualTo(Optional.of(CITY));
    }

    @Test
    void getCityDetails_whenNotFound()
    {
        // Given
        given(mockedCityRepository.findByName(NAME))
            .willReturn(Optional.empty());

        // When
        Optional<CityEntity> city = domain.getCity(NAME);

        // Then
        assertThat(city).isEqualTo(Optional.empty());
    }

    @Test
    void updateCity()
    {
        // Given
        given(mockedCityRepository.findByName(NAME))
            .willReturn(Optional.of(CITY_WITH_ID));

        // When
        domain.updateCity(CITY_WITH_ID);

        // Then
        verify(mockedCityRepository).findByName(NAME);
        verify(mockedCityRepository).save(CITY_WITH_ID);
    }

    @Test
    void updateCity_whenDuplicate_exception()
    {
        // Given
        given(mockedCityRepository.findByName(NAME))
            .willReturn(Optional.of(CITY_WITH_ID2));

        // When
        Throwable exception = catchThrowable(() ->
            domain.updateCity(CITY_WITH_ID));

        // Then
        verify(mockedCityRepository, never()).save(any());

        assertThat(exception).isInstanceOf(CityAlreadyExistsException.class);
    }

    @Test
    void updateCity_withNoId_exception()
    {
        // When
        Throwable exception = catchThrowable(() ->
            domain.updateCity(new CityEntity()));

        // Then
        verify(mockedCityRepository, never()).save(any());
        assertThat(exception).isInstanceOf(EntityNotFoundException.class);
    }
}
