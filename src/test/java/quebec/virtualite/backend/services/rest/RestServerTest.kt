package quebec.virtualite.backend.services.rest

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.test.util.ReflectionTestUtils.setField
import org.springframework.web.server.ResponseStatusException
import quebec.virtualite.backend.TestConstants.BRAND
import quebec.virtualite.backend.TestConstants.BRAND2
import quebec.virtualite.backend.TestConstants.ID
import quebec.virtualite.backend.TestConstants.NAME
import quebec.virtualite.backend.TestConstants.NAME2
import quebec.virtualite.backend.TestConstants.WHEEL
import quebec.virtualite.backend.TestConstants.WHEEL2
import quebec.virtualite.backend.services.domain.DomainService
import quebec.virtualite.backend.services.domain.entities.WheelEntity

@RunWith(MockitoJUnitRunner::class)
class RestServerTest
{
    private val EMPTY_BRAND = ""
    private val EMPTY_NAME = ""
    private val NULL_BRAND = null
    private val NULL_NAME = null

    @InjectMocks
    private lateinit var server: RestServer

    @Mock
    private lateinit var mockedDomainService: DomainService

    @Mock
    private lateinit var mockedLogger: Logger

    @Before
    fun before()
    {
        setField(server, "log", mockedLogger)
    }

    @Test
    fun addWheel()
    {
        // When
        val response = server.addWheel(WheelDTO(BRAND, NAME))

        // Then
        verify(mockedDomainService).addWheel(WheelEntity(0, BRAND, NAME))
    }

    @Test
    fun addWheel_whenDuplicate_conflict()
    {
        // Given
        given(mockedDomainService.getWheelDetails(NAME))
            .willReturn(WHEEL)

        // When
        val exception = catchThrowable {
            server.addWheel(WheelDTO(BRAND, NAME))
        }

        // Then
        verify(mockedDomainService).getWheelDetails(NAME)
        verify(mockedDomainService, never()).addWheel(WheelEntity(0, BRAND, NAME))

        assertStatus(exception, CONFLICT)
    }

    @Test
    fun addWheel_withEmptyField_badRequest()
    {
        addWheel_withEmptyField(BRAND, EMPTY_NAME)
        addWheel_withEmptyField(BRAND, NULL_NAME)
        addWheel_withEmptyField(EMPTY_BRAND, NAME)
        addWheel_withEmptyField(EMPTY_BRAND, EMPTY_NAME)
        addWheel_withEmptyField(EMPTY_BRAND, NULL_NAME)
        addWheel_withEmptyField(NULL_BRAND, NAME)
        addWheel_withEmptyField(NULL_BRAND, EMPTY_NAME)
        addWheel_withEmptyField(NULL_BRAND, NULL_NAME)
    }

    private fun addWheel_withEmptyField(brand: String?, name: String?)
    {
        // When
        val exception = catchThrowable {
            server.addWheel(WheelDTO(brand, name))
        }

        // Then
        assertStatus(exception, BAD_REQUEST)
    }

    @Test
    fun deleteWheel()
    {
        // Given
        given(mockedDomainService.getWheelDetails(NAME))
            .willReturn(WHEEL)

        // When
        val response = server.deleteWheel(NAME)

        // Then
        verify(mockedDomainService).getWheelDetails(NAME)
        verify(mockedDomainService).deleteWheel(NAME)
    }

    @Test
    fun deleteWheel_whenNameIsNull_log()
    {
        // When
        val exception = catchThrowable {
            server.deleteWheel(NULL_NAME)
        }

        // Then
        verify(mockedLogger).warn("name is not specified")

        assertStatus(exception, BAD_REQUEST)
    }

    @Test
    fun deleteWheel_whenNotFound()
    {
        // Given
        given(mockedDomainService.getWheelDetails(NAME))
            .willReturn(null)

        // When
        val exception = catchThrowable {
            server.deleteWheel(NAME)
        }

        // Then
        verify(mockedDomainService).getWheelDetails(NAME)
        verify(mockedDomainService, never()).deleteWheel(NAME)

        assertStatus(exception, NOT_FOUND)
    }

    @Test
    fun getAllWheelDetails()
    {
        // Given
        given(mockedDomainService.getAllWheelDetails())
            .willReturn(listOf(WHEEL, WHEEL2))

        // When
        val response = server.getAllWheelDetails()

        // Then
        verify(mockedDomainService).getAllWheelDetails()

        assertThat(response).isEqualTo(
            arrayOf(
                WheelDTO(BRAND, NAME),
                WheelDTO(BRAND2, NAME2)
            )
        )
    }

    @Test
    fun getWheelDetails()
    {
        // Given
        given(mockedDomainService.getWheelDetails(NAME))
            .willReturn(WHEEL)

        // When
        val response = server.getWheelDetails(NAME)

        // Then
        verify(mockedDomainService).getWheelDetails(NAME)

        assertThat(response).isEqualTo(WheelDTO(BRAND, NAME))
    }

    @Test
    fun getWheelDetails_whenNameIsNull_log()
    {
        // When
        val exception = catchThrowable {
            server.getWheelDetails(NULL_NAME)
        }

        // Then
        verify(mockedLogger).warn("name is not specified")

        assertStatus(exception, BAD_REQUEST)
    }

    @Test
    fun getWheelDetails_whenNotFound()
    {
        // Given
        given(mockedDomainService.getWheelDetails(NAME))
            .willReturn(null)

        // When
        val exception = catchThrowable {
            server.getWheelDetails(NAME)
        }

        // Then
        assertStatus(exception, NOT_FOUND)
    }

    @Test
    fun updateWheel()
    {
        // Given
        given(mockedDomainService.getWheelDetails(NAME))
            .willReturn(WHEEL)

        // When
        val response = server.updateWheel(NAME, WheelDTO(BRAND2, NAME2))

        // Then
        verify(mockedDomainService).getWheelDetails(NAME)
        verify(mockedDomainService).saveWheel(WheelEntity(ID, BRAND2, NAME2))
    }

    private fun assertStatus(exception: Throwable?, expectedStatus: HttpStatus)
    {
        assertThat(exception)
            .isInstanceOf(ResponseStatusException::class.java)
            .hasFieldOrPropertyWithValue("status", expectedStatus)
    }
}
