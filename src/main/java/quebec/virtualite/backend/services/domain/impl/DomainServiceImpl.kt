package quebec.virtualite.backend.services.domain.impl

import org.springframework.stereotype.Service
import quebec.virtualite.backend.services.domain.DomainService
import quebec.virtualite.backend.services.domain.WheelAlreadyExistsException
import quebec.virtualite.backend.services.domain.entities.WheelEntity
import quebec.virtualite.backend.services.domain.repositories.WheelRepository
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
open class DomainServiceImpl(
    private val wheelRepository: WheelRepository

) : DomainService
{
    @Transactional
    override fun addWheel(wheel: WheelEntity)
    {
        if (wheelRepository.findByName(wheel.name) != null)
            throw WheelAlreadyExistsException()

        wheelRepository.save(wheel)
    }

    @Transactional
    override fun deleteAll()
    {
        wheelRepository.deleteAll()
    }

    @Transactional
    override fun deleteWheel(name: String)
    {
        wheelRepository.deleteByName(name)
    }

    override fun getAllWheelDetails(): List<WheelEntity>
    {
        return wheelRepository.findAll()
    }

    override fun getWheelDetails(wheelName: String): WheelEntity?
    {
        return wheelRepository.findByName(wheelName)
    }

    @Transactional
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