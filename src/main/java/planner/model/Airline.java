package planner.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "airlines")
@Getter
@Setter
@NoArgsConstructor
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String leonSubDomain;

    @Column(unique = true, nullable = false)
    private String leonApiKey;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "("
                + "name = " + name + ", "
                + "leonSubDomain = " + leonSubDomain + ")";
    }
}
