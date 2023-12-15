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

import static quebec.virtualite.backend.services.domain.database.DatabaseTables.WHEELS_SEQUENCE;
import static quebec.virtualite.backend.services.domain.database.DatabaseTables.WHEELS_TABLE;

@Entity
@Table(name = WHEELS_TABLE)
@Data
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class WheelEntity
{
    @Id
    @SequenceGenerator(name = WHEELS_SEQUENCE, sequenceName = WHEELS_SEQUENCE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = WHEELS_SEQUENCE)
    private long id;

    private String brand;
    private String name;
}
