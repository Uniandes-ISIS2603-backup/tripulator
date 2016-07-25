/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.tripulator.persistence;

import co.edu.uniandes.csw.tripulator.entities.TripEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Stateless
public class TripPersistence {
    
        private static final Logger logger = Logger.getLogger(TripPersistence.class.getName());
        
        @PersistenceContext(unitName = "TripulatorPU")
        protected EntityManager em;
        
        public TripEntity create(TripEntity entity){
            logger.info("Creating a new trip.");
            em.persist(entity);
            logger.info("A new trip has been created.");
            return entity;
        }
        
        public TripEntity update(TripEntity entity){
            logger.log(Level.INFO, "Updating trip with id={0}",entity.getId());
            return em.merge(entity);
        }
        
        public void delete(Long idTrip){
            logger.log(Level.INFO, "Deleting trip with id={0}",idTrip);
            TripEntity entity = em.find(TripEntity.class, idTrip);
            em.remove(entity);
        }
        
        public TripEntity find(Long idTraveller, Long idTrip){
            logger.log(Level.INFO, "Finding trip with idTraveller= {0} and idTrip= {1}", new Object[]{idTraveller, idTrip});
            TypedQuery<TripEntity> q = em.createQuery("select i from TripEntity i where (i.traveller.id = :idTraveller) "
                + "and (i.id = :idTrip)",TripEntity.class);
            q.setParameter("idTraveller", idTraveller);
            q.setParameter("idTrip",idTrip);
            return q.getSingleResult();
        }
        
        public List<TripEntity> findAll(Long idTraveller){
            logger.info("Finding all trips from traveller.");
            TypedQuery<TripEntity> q = em.createQuery("select u from "
                    + "TripEntity u where (u.traveller.id = :idTraveller)",
                    TripEntity.class);
            q.setParameter("idTraveller", idTraveller);
            return q.getResultList();
        }
}
