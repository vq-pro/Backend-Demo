package quebec.virtualite.backend.services.domain.entities

import quebec.virtualite.backend.services.domain.entities.CityEntity.Companion.TABLE
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = TABLE)
data class CityEntity
    (
    @Id
    @SequenceGenerator(name = SEQUENCE, sequenceName = SEQUENCE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    val id: Long = 0,

    val name: String,
    val province: String,
)
{
    companion object
    {
        const val TABLE = "cities"
        const val SEQUENCE = TABLE + "_id_seq"
    }
}
