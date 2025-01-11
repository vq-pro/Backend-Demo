package quebec.virtualite.backend.services.domain.entities

import quebec.virtualite.backend.services.domain.entities.CityEntity.Companion.TABLE
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = TABLE)
class CityEntity
    (
    @Id
    @SequenceGenerator(name = SEQUENCE, sequenceName = SEQUENCE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    var id: Long = 0,

    var name: String = "",
    var province: String = "",
)
{
    companion object
    {
        const val TABLE = "cities"
        const val SEQUENCE = TABLE + "_id_seq"
    }

    override fun equals(other: Any?): Boolean
    {
        when
        {
            this === other -> return true
            other == null || javaClass != other.javaClass -> return false
            else ->
            {
                val otherCity = other as CityEntity
                return id == otherCity.id
                    && name == otherCity.name
                    && province == otherCity.province
            }
        }
    }

    override fun hashCode(): Int
    {
        return Objects.hash(id, name, province)
    }
}
