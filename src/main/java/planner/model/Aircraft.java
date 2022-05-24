package planner.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "aircraft")
@Getter
@Setter
@NoArgsConstructor
public class Aircraft {
    @Id
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String registration;

    @Column(nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Airline airline;

    private boolean isActive;

    private boolean isAircraft;

    public void setIsAircraft(boolean isAircraft) {
        this.isAircraft = isAircraft;
    }

    public boolean getIsAircraft() {
        return isAircraft;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }
}
