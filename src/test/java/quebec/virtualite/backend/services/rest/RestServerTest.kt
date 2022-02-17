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
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.test.util.ReflectionTestUtils.setField
import quebec.virtualite.backend.services.domain.DomainService
import quebec.virtualite.backend.services.domain.entities.WheelEntity

@RunWith(MockitoJUnitRunner::class)
class RestServerTest
{
    private val ID = 111L
    private val BRAND = "Brand"
    private val NAME = "Wheel"
    private val NULL_NAME: String? = null
    private val WHEEL = WheelEntity(ID, BRAND, NAME)

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
        assertThat(response.body).isEqualTo(WheelResponse(BRAND, NAME))
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
