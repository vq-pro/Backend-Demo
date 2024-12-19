package quebec.virtualite.backend.services.rest

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.test.util.ReflectionTestUtils.setField
import quebec.virtualite.backend.TestConstants.BAD_CITY_DTO
import quebec.virtualite.backend.TestConstants.CITY
import quebec.virtualite.backend.TestConstants.CITY2
import quebec.virtualite.backend.TestConstants.CITY_DTO
import quebec.virtualite.backend.TestConstants.CITY_DTO2
import quebec.virtualite.backend.TestConstants.CITY_WITH_ID
import quebec.virtualite.backend.TestConstants.ID
import quebec.virtualite.backend.TestConstants.NAME
import quebec.virtualite.backend.TestConstants.NAME2
import quebec.virtualite.backend.TestConstants.PROVINCE2
import quebec.virtualite.backend.services.domain.DomainService
import quebec.virtualite.backend.services.domain.entities.CityEntity
import quebec.virtualite.backend.services.rest.impl.RestServer
import quebec.virtualite.backend.services.utils.TestUtils.assertInvalid
import quebec.virtualite.backend.services.utils.TestUtils.assertStatus
import quebec.virtualite.backend.services.utils.TestUtils.assertValid

@ExtendWith(MockitoExtension::class)
class RestServerTest
{
    @InjectMocks
    private lateinit var server: RestServer

    @Mock
    private lateinit var mockedDomainService: DomainService

    @Mock
    private lateinit var mockedLogger: Logger

    @BeforeEach
    fun before()
    {
        setField(server, "log", mockedLogger)
    }

    @Test
    fun addCity()
    {
        // When
        server.addCity(CITY_DTO)

        // Then
        verify(mockedDomainService).addCity(CITY)
    }

    @Test
    fun deleteCity()
    {
        // Given
        given(mockedDomainService.getCityDetails(NAME))
            .willReturn(CITY)

        // When
        server.deleteCity(NAME)

        // Then
        verify(mockedDomainService).getCityDetails(NAME)
        verify(mockedDomainService).deleteCity(NAME)
    }

    @Test
    fun deleteCity_whenNotFound()
    {
        // Given
        given(mockedDomainService.getCityDetails(NAME))
            .willReturn(null)

        // When
        val exception = catchThrowable {
            server.deleteCity(NAME)
        }

        // Then
        verify(mockedDomainService).getCityDetails(NAME)
        verify(mockedDomainService, never()).deleteCity(NAME)

        assertStatus(exception, NOT_FOUND)
    }

    @Test
    fun getCityDetails()
    {
        // Given
        given(mockedDomainService.getCityDetails(NAME))
            .willReturn(CITY)

        // When
        val response = server.getCityDetails(NAME)

        // Then
        verify(mockedDomainService).getCityDetails(NAME)

        assertThat(response).isEqualTo(CITY_DTO)
    }

    @Test
    fun getCitiesDetails()
    {
        // Given
        given(mockedDomainService.getCitiesDetails())
            .willReturn(listOf(CITY, CITY2))

        // When
        val response = server.getCitiesDetails()

        // Then
        verify(mockedDomainService).getCitiesDetails()

        assertThat(response).isEqualTo(
            listOf(
                CITY_DTO,
                CITY_DTO2
            )
        )
    }

    @Test
    fun getCityDetails_whenNotFound()
    {
        // Given
        given(mockedDomainService.getCityDetails(NAME))
            .willReturn(null)

        // When
        val exception = catchThrowable {
            server.getCityDetails(NAME)
        }

        // Then
        assertStatus(exception, NOT_FOUND)
    }

    @Test
    fun updateCity()
    {
        // Given
        given(mockedDomainService.getCityDetails(NAME))
            .willReturn(CITY_WITH_ID)

        // When
        server.updateCity(NAME, CITY_DTO2)

        // Then
        verify(mockedDomainService).getCityDetails(NAME)
        verify(mockedDomainService).updateCity(CityEntity(ID, NAME2, PROVINCE2))
    }

    @Test
    fun updateCity_whenNotFound()
    {
        // Given
        given(mockedDomainService.getCityDetails(NAME))
            .willReturn(null)

        // When
        val exception = catchThrowable {
            server.updateCity(NAME, CITY_DTO)
        }

        // Then
        assertStatus(exception, NOT_FOUND)
    }

    @Test
    fun validate()
    {
        assertValid(server, "addCity", CITY_DTO)
        assertInvalid(server, "addCity", BAD_CITY_DTO)
        assertInvalid(server, "addCity", null)
        assertInvalid(server, "addCity", "")
        assertInvalid(server, "addCity", 10f)

        assertValid(server, "deleteCity", NAME)
        assertInvalid(server, "deleteCity", null)
        assertInvalid(server, "deleteCity", "")
        assertInvalid(server, "deleteCity", 10f)

        assertValid(server, "getCityDetails", NAME)
        assertInvalid(server, "getCityDetails", null)
        assertInvalid(server, "getCityDetails", "")
        assertInvalid(server, "getCityDetails", 10f)

        assertValid(server, "updateCity", NAME, CITY_DTO)
        assertInvalid(server, "updateCity", NAME, BAD_CITY_DTO)
        assertInvalid(server, "updateCity", "", BAD_CITY_DTO)
        assertInvalid(server, "updateCity", null, CITY_DTO)
        assertInvalid(server, "updateCity", "", CITY_DTO)
        assertInvalid(server, "updateCity", 10f, CITY_DTO)
    }
}
