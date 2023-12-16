package quebec.virtualite.backend.services.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import quebec.virtualite.backend.TestConstants.NAME
import quebec.virtualite.backend.TestConstants.WHEEL
import quebec.virtualite.backend.TestConstants.WHEEL2
import quebec.virtualite.backend.TestConstants.WHEEL_WITH_ID
import quebec.virtualite.backend.TestConstants.WHEEL_WITH_ID2
import quebec.virtualite.backend.services.domain.entities.WheelEntity
import quebec.virtualite.backend.services.domain.impl.DomainServiceImpl
import quebec.virtualite.backend.services.domain.repositories.WheelRepository
import javax.persistence.EntityNotFoundException

@RunWith(MockitoJUnitRunner::class)
class DomainServiceImplTest
{
    @InjectMocks
    private lateinit var domain: DomainServiceImpl

    @Mock
    private lateinit var mockedWheelRepository: WheelRepository

    @Test
    fun addWheel()
    {
        // When
        domain.addWheel(WHEEL)

        // Then
        verify(mockedWheelRepository).save(WHEEL)
    }

    @Test
    fun addWheel_withDuplicate_exception()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(WHEEL_WITH_ID)

        // When
        val exception = catchThrowable {
            domain.addWheel(WHEEL_WITH_ID)
        }

        // Then
        verify(mockedWheelRepository).findByName(NAME)
        verify(mockedWheelRepository, never()).save(WHEEL_WITH_ID)

        assertThat(exception).isInstanceOf(WheelAlreadyExistsException::class.java)
    }

    @Test
    fun deleteWheel()
    {
        // When
        domain.deleteWheel(NAME)

        // Then
        verify(mockedWheelRepository).deleteByName(NAME)
    }

    @Test
    fun deleteAll()
    {
        // When
        domain.deleteAll()

        // Then
        verify(mockedWheelRepository).deleteAll()
    }

    @Test
    fun getWheelDetails()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(WHEEL)

        // When
        val response = domain.getWheelDetails(NAME)

        // Then
        verify(mockedWheelRepository).findByName(NAME)

        assertThat(response).isEqualTo(WHEEL)
    }

    @Test
    fun getWheelsDetails()
    {
        // Given
        given(mockedWheelRepository.findAll())
            .willReturn(listOf(WHEEL, WHEEL2))

        // When
        val response = domain.getWheelsDetails()

        // Then
        verify(mockedWheelRepository).findAll()

        assertThat(response).isEqualTo(listOf(WHEEL, WHEEL2))
    }

    @Test
    fun getWheelDetails_whenNotFound()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(null)

        // When
        val wheel = domain.getWheelDetails(NAME)

        // Then
        assertThat(wheel).isEqualTo(null)
    }

    @Test
    fun updateWheel()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(WHEEL_WITH_ID)

        // When
        domain.updateWheel(WHEEL_WITH_ID)

        // Then
        verify(mockedWheelRepository).findByName(NAME)
        verify(mockedWheelRepository).save(WHEEL_WITH_ID)
    }

    @Test
    fun updateWheel_whenDuplicate_exception()
    {
        // Given
        given(mockedWheelRepository.findByName(NAME))
            .willReturn(WHEEL_WITH_ID2)

        // When
        val exception = catchThrowable {
            domain.updateWheel(WHEEL_WITH_ID)
        }

        // Then
        verify(mockedWheelRepository, never()).save(any(WheelEntity::class.java))

        assertThat(exception).isInstanceOf(WheelAlreadyExistsException::class.java)
    }

    @Test
    fun updateWheel_withNoId_exception()
    {
        // When
        val exception = catchThrowable {
            domain.updateWheel(WHEEL)
        }

        // Then
        verify(mockedWheelRepository, never()).save(any(WheelEntity::class.java))

        assertThat(exception).isInstanceOf(EntityNotFoundException::class.java)
    }
}
