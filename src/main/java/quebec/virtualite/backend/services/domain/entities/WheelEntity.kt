package quebec.virtualite.backend.services.domain.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "wheels")
class WheelEntity
    (
    @Id
    @SequenceGenerator(name = "wheels_id_seq", sequenceName = "wheels_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wheels_id_seq")
    val id: Long = 0,
    val brand: String,
    val name: String
)
{
    override fun equals(other: Any?): Boolean
    {
        val otherWheel: WheelEntity = other as WheelEntity
        return id == otherWheel.id
            && brand == otherWheel.brand
            && name == otherWheel.name
    }

    override fun hashCode(): Int
    {
        var result = id.hashCode()
        result = 31 * result + brand.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
