package quebec.virtualite.backend.services.domain.impl

import org.springframework.stereotype.Service
import quebec.virtualite.backend.services.domain.DomainService
import quebec.virtualite.backend.services.domain.WheelAlreadyExistsException
import quebec.virtualite.backend.services.domain.entities.WheelEntity
import quebec.virtualite.backend.services.domain.repositories.WheelRepository
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
@Transactional
open class DomainServiceImpl(
    private val wheelRepository: WheelRepository

) : DomainService
{
    override fun addWheel(wheel: WheelEntity)
    {
        if (wheelRepository.findByName(wheel.name) != null)
            throw WheelAlreadyExistsException()

        wheelRepository.save(wheel)
    }

    override fun deleteAll()
    {
        wheelRepository.deleteAll()
    }

    override fun deleteWheel(name: String)
    {
        wheelRepository.deleteByName(name)
    }

    override fun getWheelDetails(wheelName: String)
        : WheelEntity?
    {
        return wheelRepository.findByName(wheelName)
    }

    override fun getWheelsDetails()
        : List<WheelEntity>
    {
        return wheelRepository.findAll()
    }

    override fun updateWheel(wheel: WheelEntity)
    {
        if (wheel.id == 0L)
        {
            throw EntityNotFoundException()
        }

        val existingWheel = wheelRepository.findByName(wheel.name)
        if (existingWheel != null && existingWheel.id != wheel.id)
        {
            throw WheelAlreadyExistsException()
        }

        wheelRepository.save(wheel)
    }
}
