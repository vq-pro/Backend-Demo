package quebec.virtualite.backend.services.domain.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "wheels")
@Data
@Accessors(chain = true)
public class WheelEntity
{
    @Id
    @SequenceGenerator(name = "wheels_id_seq", sequenceName = "wheels_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wheels_id_seq")
    private long id;

    private String brand;
    private String name;
}