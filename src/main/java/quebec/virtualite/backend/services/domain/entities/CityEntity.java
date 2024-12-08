package quebec.virtualite.backend.services.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import static quebec.virtualite.backend.services.domain.entities.CityEntity.TABLE;

@Entity
@Table(name = TABLE)
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
