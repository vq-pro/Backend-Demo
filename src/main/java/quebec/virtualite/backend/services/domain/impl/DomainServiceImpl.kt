package quebec.virtualite.backend.services.domain.impl

import org.springframework.stereotype.Service
import quebec.virtualite.backend.services.domain.DomainService
import quebec.virtualite.backend.services.domain.WheelAlreadyExistsException
import quebec.virtualite.backend.services.domain.entities.WheelEntity
import quebec.virtualite.backend.services.domain.repositories.WheelRepository

@Service
class DomainServiceImpl(
    private val wheelRepository: WheelRepository

) : DomainService
{
    override fun deleteAll()
    {
        wheelRepository.deleteAll()
    }

    override fun getWheelDetails(wheelName: String): WheelEntity?
    {
        return wheelRepository.findByName(wheelName)
    }

    override fun saveWheel(wheel: WheelEntity)
    {
        if (wheelRepository.findByName(wheel.name) != null)
        {
            throw WheelAlreadyExistsException()
        }

        wheelRepository.save(wheel)
    }
}
