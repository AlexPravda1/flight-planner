package planner.dao.impl;

import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import planner.dao.RoleDao;
import planner.exception.DataProcessingException;
import planner.model.Role;
import planner.model.UserRoleName;

@Repository
public class RoleDaoImpl extends AbstractDao<Role, Long> implements RoleDao {
    @Autowired
    protected RoleDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Role.class);
    }

    @Override
    public Optional<Role> getRoleByName(String roleName) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Role r WHERE r.roleName = :roleName", Role.class)
                    .setParameter("roleName", UserRoleName.valueOf(roleName))
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Couldn't get role by role name: " + roleName, e);
        }
    }
}
