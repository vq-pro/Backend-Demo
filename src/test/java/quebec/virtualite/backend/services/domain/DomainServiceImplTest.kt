package quebec.virtualite.backend.services.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import quebec.virtualite.backend.TestConstants.CITY
import quebec.virtualite.backend.TestConstants.CITY2
import quebec.virtualite.backend.TestConstants.CITY_WITH_ID
import quebec.virtualite.backend.TestConstants.CITY_WITH_ID2
import quebec.virtualite.backend.TestConstants.NAME
import quebec.virtualite.backend.services.domain.entities.CityEntity
import quebec.virtualite.backend.services.domain.impl.DomainServiceImpl
import quebec.virtualite.backend.services.domain.repositories.CityRepository
import javax.persistence.EntityNotFoundException

@ExtendWith(MockitoExtension::class)
@Tag("UnitTest")
class DomainServiceImplTest
{
    @InjectMocks
    private lateinit var domain: DomainServiceImpl

    @Mock
    private lateinit var mockedCityRepository: CityRepository

    @Test
    fun addCity()
    {
        // When
        domain.addCity(CITY)

        // Then
        verify(mockedCityRepository).save(CITY)
    }

    @Test
    fun addCity_withDuplicate_exception()
    {
        // Given
        given(mockedCityRepository.findByName(NAME))
            .willReturn(CITY_WITH_ID)

        // When
        val exception = catchThrowable {
            domain.addCity(CITY_WITH_ID)
        }

        // Then
        verify(mockedCityRepository).findByName(NAME)
        verify(mockedCityRepository, never()).save(CITY_WITH_ID)

        assertThat(exception).isInstanceOf(CityAlreadyExistsException::class.java)
    }

    @Test
    fun deleteCity()
    {
        // When
        domain.deleteCity(NAME)

        // Then
        verify(mockedCityRepository).deleteByName(NAME)
    }

    @Test
    fun deleteAll()
    {
        // When
        domain.deleteAll()

        // Then
        verify(mockedCityRepository).deleteAll()
    }

    @Test
    fun getCityDetails()
    {
        // Given
        given(mockedCityRepository.findByName(NAME))
            .willReturn(CITY)

        // When
        val response = domain.getCityDetails(NAME)

        // Then
        verify(mockedCityRepository).findByName(NAME)

        assertThat(response).isEqualTo(CITY)
    }

    @Test
    fun getCitiesDetails()
    {
        // Given
        given(mockedCityRepository.findAllByOrderByNameAscProvinceAsc())
            .willReturn(listOf(CITY, CITY2))

        // When
        val response = domain.getCitiesDetails()

        // Then
        verify(mockedCityRepository).findAllByOrderByNameAscProvinceAsc()

        assertThat(response).isEqualTo(listOf(CITY, CITY2))
    }

    @Test
    fun getCityDetails_whenNotFound()
    {
        // Given
        given(mockedCityRepository.findByName(NAME))
            .willReturn(null)

        // When
        val city = domain.getCityDetails(NAME)

        // Then
        assertThat(city).isEqualTo(null)
    }

    @Test
    fun updateCity()
    {
        // Given
        given(mockedCityRepository.findByName(NAME))
            .willReturn(CITY_WITH_ID)

        // When
        domain.updateCity(CITY_WITH_ID)

        // Then
        verify(mockedCityRepository).findByName(NAME)
        verify(mockedCityRepository).save(CITY_WITH_ID)
    }

    @Test
    fun updateCity_whenDuplicate_exception()
    {
        // Given
        given(mockedCityRepository.findByName(NAME))
            .willReturn(CITY_WITH_ID2)

        // When
        val exception = catchThrowable {
            domain.updateCity(CITY_WITH_ID)
        }

        // Then
        verify(mockedCityRepository, never()).save(any(CityEntity::class.java))

        assertThat(exception).isInstanceOf(CityAlreadyExistsException::class.java)
    }

    @Test
    fun updateCity_withNoId_exception()
    {
        // When
        val exception = catchThrowable {
            domain.updateCity(CITY)
        }

        // Then
        verify(mockedCityRepository, never()).save(any(CityEntity::class.java))

        assertThat(exception).isInstanceOf(EntityNotFoundException::class.java)
    }
}
