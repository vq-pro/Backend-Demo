package quebec.virtualite.backend.services.domain.entities

import quebec.virtualite.backend.services.domain.entities.WheelEntity.Companion.TABLE
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = TABLE)
class WheelEntity
    (
    @Id
    @SequenceGenerator(name = SEQUENCE, sequenceName = SEQUENCE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    val id: Long = 0,

    val brand: String,
    val name: String,
)
{
    companion object
    {
        const val TABLE = "wheels"
        const val SEQUENCE = TABLE + "_id_seq"
    }

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
