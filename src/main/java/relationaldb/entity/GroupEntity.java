package relationaldb.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "groups")
@Data
public class GroupEntity {
    @Id
    private String name;
    private Integer frequencyInDays;
}
