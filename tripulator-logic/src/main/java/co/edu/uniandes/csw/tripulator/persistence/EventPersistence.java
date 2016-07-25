package co.edu.uniandes.csw.tripulator.persistence;

import co.edu.uniandes.csw.tripulator.entities.EventEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class EventPersistence {

    private static final Logger LOGGER = Logger.getLogger(EventPersistence.class.getName());

    @PersistenceContext(unitName = "TripulatorPU")
    protected EntityManager em;

    public List<EventEntity> findAll(Long idDay) {
        LOGGER.info("Getting all events");
        TypedQuery<EventEntity> q = em.createQuery("select u from "
                + "EventEntity u where (u.day.id = :idDay)",
                EventEntity.class);
        q.setParameter("idDay", idDay);
        return q.getResultList();
    }

    public EventEntity find(Long idDay, Long idEvent) {
        LOGGER.log(Level.INFO, "Searching for event with id={0}", idEvent);
        TypedQuery<EventEntity> q = em.createQuery("select u from "
                + "EventEntity u where (u.day.id = :idDay) and (u.id = :idEvent)",
                EventEntity.class);
        q.setParameter("idDay", idDay);
        q.setParameter("idEvent", idEvent);
        return q.getSingleResult();
    }

    public EventEntity create(EventEntity entity) {
        LOGGER.info("Creating a new Event");
        em.persist(entity);
        LOGGER.info("Event created");
        return entity;
    }

    public EventEntity update(EventEntity entity) {
        LOGGER.log(Level.INFO, "Updating event with id={0}", entity.getId());
        return em.merge(entity);
    }

    public void delete(Long id) {
        LOGGER.log(Level.INFO, "Deleting event with id={0}", id);
        EventEntity entity = em.find(EventEntity.class, id);
        em.remove(entity);
    }
}
