package planner.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import planner.exception.DataProcessingException;

@RequiredArgsConstructor
public abstract class AbstractDao<T, I extends Serializable> {
    protected final SessionFactory sessionFactory;
    protected final Class<T> clazz;

    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    public T save(T entity) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't create entity: " + entity, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<T> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from " + clazz.getSimpleName(), clazz).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all entities: "
                    + clazz.getSimpleName(), e);
        }
    }

    public Optional<T> findById(I id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(clazz, id));
        } catch (Exception e) {
            throw new DataProcessingException("Can't get entity: " + clazz.getSimpleName()
                    + " by id " + id, e);
        }
    }

    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    public void delete(I id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.delete(session.load(clazz, id));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't delete entity by id: " + id, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    public T update(T entity) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update entity: " + entity, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return entity;
    }
}
