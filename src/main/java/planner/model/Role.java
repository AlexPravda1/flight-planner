package planner.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "role_name", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleName roleName;

    public Role(UserRoleName roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "("
                + "roleName = " + roleName.name() + ")";
    }
}
