package co.edu.uniandes.csw.tripulator.persistence;

import co.edu.uniandes.csw.tripulator.entities.DayEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class DayPersistence {

    private static final Logger logger = Logger.getLogger(DayPersistence.class.getName());

    @PersistenceContext(unitName = "TripulatorPU")
    protected EntityManager em;

    public DayEntity create(DayEntity entity) {
        logger.info("Creating a new day.");
        em.persist(entity);
        logger.info("Day created");
        return entity;
    }

    public DayEntity update(DayEntity entity) {
        logger.log(Level.INFO, "Updating day with id={0}", entity.getId());
        return em.merge(entity);
    }

    public void delete(Long id) {
        logger.log(Level.INFO, "Deleting day with id={0}", id);
        DayEntity entity = em.find(DayEntity.class, id);
        em.remove(entity);
    }

    public DayEntity find(Long idTrip, Long idDay) {
        logger.log(Level.INFO, "Searching for day with id={0}", idDay);
        TypedQuery<DayEntity> q = em.createQuery("select u from "
                + "DayEntity u where (u.trip.id = :idTrip) and (u.id = :idDay)",
                DayEntity.class);
        q.setParameter("idTrip", idTrip);
        q.setParameter("idDay", idDay);
        return q.getSingleResult();
    }

    public List<DayEntity> findAll(Long idTrip) {
        logger.info("Getting all days");
        TypedQuery<DayEntity> q = em.createQuery("select u from "
                + "DayEntity u where (u.trip.id = :idTrip)",
                DayEntity.class);
        q.setParameter("idTrip", idTrip);
        return q.getResultList();
    }
}
