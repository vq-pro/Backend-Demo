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
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.test.util.ReflectionTestUtils.setField
import quebec.virtualite.backend.TestConstants.BAD_WHEEL_DTO
import quebec.virtualite.backend.TestConstants.BRAND
import quebec.virtualite.backend.TestConstants.BRAND2
import quebec.virtualite.backend.TestConstants.ID
import quebec.virtualite.backend.TestConstants.NAME
import quebec.virtualite.backend.TestConstants.NAME2
import quebec.virtualite.backend.TestConstants.WHEEL
import quebec.virtualite.backend.TestConstants.WHEEL2
import quebec.virtualite.backend.TestConstants.WHEEL_DTO
import quebec.virtualite.backend.TestConstants.WHEEL_DTO2
import quebec.virtualite.backend.TestConstants.WHEEL_WITH_ID
import quebec.virtualite.backend.services.domain.DomainService
import quebec.virtualite.backend.services.domain.entities.WheelEntity
import quebec.virtualite.backend.services.rest.impl.RestServer
import quebec.virtualite.backend.services.utils.TestUtils.assertInvalid
import quebec.virtualite.backend.services.utils.TestUtils.assertStatus
import quebec.virtualite.backend.services.utils.TestUtils.assertValid

@RunWith(MockitoJUnitRunner::class)
class RestServerTest
{
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
        server.addWheel(WHEEL_DTO)

        // Then
        verify(mockedDomainService).addWheel(WheelEntity(0, BRAND, NAME))
    }

    @Test
    fun deleteWheel()
    {
        // Given
        given(mockedDomainService.getWheelDetails(NAME))
            .willReturn(WHEEL)

        // When
        server.deleteWheel(NAME)

        // Then
        verify(mockedDomainService).getWheelDetails(NAME)
        verify(mockedDomainService).deleteWheel(NAME)
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
            listOf(
                WHEEL_DTO,
                WHEEL_DTO2
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

        assertThat(response).isEqualTo(WHEEL_DTO)
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
            .willReturn(WHEEL_WITH_ID)

        // When
        server.updateWheel(NAME, WHEEL_DTO2)

        // Then
        verify(mockedDomainService).getWheelDetails(NAME)
        verify(mockedDomainService).updateWheel(WheelEntity(ID, BRAND2, NAME2))
    }

    @Test
    fun updateWheel_whenNotFound()
    {
        // Given
        given(mockedDomainService.getWheelDetails(NAME))
            .willReturn(null)

        // When
        val exception = catchThrowable {
            server.updateWheel(NAME, WHEEL_DTO)
        }

        // Then
        assertStatus(exception, NOT_FOUND)
    }

    @Test
    fun validate()
    {
        assertValid(server, "addWheel", WHEEL_DTO)
        assertInvalid(server, "addWheel", BAD_WHEEL_DTO)
        assertInvalid(server, "addWheel", null)
        assertInvalid(server, "addWheel", "")
        assertInvalid(server, "addWheel", 10f)

        assertValid(server, "deleteWheel", NAME)
        assertInvalid(server, "deleteWheel", null)
        assertInvalid(server, "deleteWheel", "")
        assertInvalid(server, "deleteWheel", 10f)

        assertValid(server, "getWheelDetails", NAME)
        assertInvalid(server, "getWheelDetails", null)
        assertInvalid(server, "getWheelDetails", "")
        assertInvalid(server, "getWheelDetails", 10f)

        assertValid(server, "updateWheel", NAME, WHEEL_DTO)
        assertInvalid(server, "updateWheel", NAME, BAD_WHEEL_DTO)
        assertInvalid(server, "updateWheel", "", BAD_WHEEL_DTO)
        assertInvalid(server, "updateWheel", null, WHEEL_DTO)
        assertInvalid(server, "updateWheel", "", WHEEL_DTO)
        assertInvalid(server, "updateWheel", 10f, WHEEL_DTO)
    }
}
