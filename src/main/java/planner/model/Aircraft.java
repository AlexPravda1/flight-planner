package planner.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "aircraft")
@Getter
@Setter
@NoArgsConstructor
public class Aircraft {
    @Id
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
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
