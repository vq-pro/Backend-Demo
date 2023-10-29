package quebec.virtualite.backend.services.domain

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import quebec.virtualite.backend.TestConstants.NAME
import quebec.virtualite.backend.TestConstants.WHEEL
import quebec.virtualite.backend.TestConstants.WHEEL2
import quebec.virtualite.backend.services.domain.impl.DomainServiceImpl
import quebec.virtualite.backend.services.domain.repositories.WheelRepository

@RunWith(MockitoJUnitRunner::class)
class DomainServiceImplTest
{
    @InjectMocks
    private lateinit var domainService: DomainServiceImpl

    @Mock
    private lateinit var mockedWheelRepository: WheelRepository

    @Test
    fun addWheel()
    {
        // When
        domainService.addWheel(WHEEL)

        // Then
        verify(mockedWheelRepository).save(WHEEL)
    }

    @Test
    fun deleteAll()
    {
        // When
        domainService.deleteAll()

        // Then
        verify(mockedWheelRepository).deleteAll()
    }

    @Test
    fun getAllWheelDetails()
    {
        // Given
        given(mockedWheelRepository.findAll())
            .willReturn(listOf(WHEEL, WHEEL2))

        // When
        val response = domainService.getAllWheelDetails()

        // Then
        verify(mockedWheelRepository).findAll()
        assertThat(response).isEqualTo(listOf(WHEEL, WHEEL2))
    }

    @Test
    fun getWheelDetails()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(WHEEL)

        // When
        val response = domainService.getWheelDetails(NAME)

        // Then
        verify(mockedWheelRepository).findByName(NAME)
        assertThat(response).isEqualTo(WHEEL)
    }

    @Test
    fun getWheelDetails_whenNotFound()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(null)

        // When
        val wheel = domainService.getWheelDetails(NAME)

        // Then
        assertThat(wheel).isEqualTo(null)
    }

    @Test
    fun saveWheel()
    {
        // When
        domainService.saveWheel(WHEEL)

        // Then
        verify(mockedWheelRepository).findByName(NAME)
        verify(mockedWheelRepository).save(WHEEL)
    }

    @Test
    fun saveWheel_whenAlreadyExists()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(WHEEL)

        // When
        val exception = Assertions.catchThrowable { domainService.saveWheel(WHEEL) }

        // Then
        assertThat(exception).isInstanceOf(WheelAlreadyExistsException::class.java)
    }
}
