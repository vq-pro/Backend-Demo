package quebec.virtualite.backend.services.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = CityEntity.TABLE)
@Data
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class CityEntity
{
    public static final String TABLE = "cities";

    @Id
    @SequenceGenerator(name = TABLE + "_id_seq", sequenceName = TABLE + "_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE + "_id_seq")
    private long id;

    private String name;
    private String province;
}
