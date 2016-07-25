package co.edu.uniandes.csw.tripulator.persistence;

import co.edu.uniandes.csw.tripulator.entities.TravellerEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class TravellerPersistence {

    private static final Logger logger = Logger.getLogger(TravellerPersistence.class.getName());

    @PersistenceContext(unitName = "TripulatorPU")
    protected EntityManager em;

    public TravellerEntity create(TravellerEntity entity) {
        logger.info("Creating a new traveller");
        em.persist(entity);
        logger.info("Traveller created");
        return entity;
    }

    public TravellerEntity update(TravellerEntity entity) {
        logger.log(Level.INFO, "Updating traveller with id={0}", entity.getId());
        return em.merge(entity);
    }

    public void delete(Long id) {
        logger.log(Level.INFO, "Deleting traveller with id={0}", id);
        TravellerEntity entity = em.find(TravellerEntity.class, id);
        em.remove(entity);
    }

    public TravellerEntity find(Long id) {
        logger.log(Level.INFO, "Getting traveller with id={0}", id);
        return em.find(TravellerEntity.class, id);
    }

    public List<TravellerEntity> findAll() {
        logger.info("Getting all travellers");
        Query q = em.createQuery("select u from TravellerEntity u");
        return q.getResultList();
    }
}
