package quebec.virtualite.backend.services.rest

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.slf4j.Logger
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.test.util.ReflectionTestUtils.setField
import quebec.virtualite.backend.TestConstants.BRAND
import quebec.virtualite.backend.TestConstants.BRAND2
import quebec.virtualite.backend.TestConstants.NAME
import quebec.virtualite.backend.TestConstants.NAME2
import quebec.virtualite.backend.TestConstants.WHEEL
import quebec.virtualite.backend.TestConstants.WHEEL2
import quebec.virtualite.backend.services.domain.DomainService
import quebec.virtualite.backend.services.domain.entities.WheelEntity

@RunWith(MockitoJUnitRunner::class)
class RestServerTest
{
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

        assertThat(response.statusCode).isEqualTo(CREATED)
    }

    @Test
    fun deleteWheel()
    {
        // When
        val response = server.deleteWheel(NAME)

        // Then
        verify(mockedDomainService).deleteWheel(NAME)

        assertThat(response.statusCode).isEqualTo(OK)
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

        assertThat(response.statusCode).isEqualTo(CREATED)
        assertThat(response.body).isEqualTo(
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

        assertThat(response.statusCode).isEqualTo(OK)
        assertThat(response.body).isEqualTo(WheelDTO(BRAND, NAME))
    }

    @Test
    fun getWheelDetails_whenNameIsNull_log()
    {
        // When
        val response = server.getWheelDetails(NULL_NAME)

        // Then
        assertThat(response.statusCode).isEqualTo(BAD_REQUEST)
        verify(mockedLogger).warn("name is not specified")
    }

    @Test
    fun getWheelDetails_whenNotFound()
    {
        // Given
        given(mockedDomainService.getWheelDetails(NAME))
            .willReturn(null)

        // When
        val response = server.getWheelDetails(NAME)

        // Then
        assertThat(response.statusCode).isEqualTo(NOT_FOUND)
    }
}
