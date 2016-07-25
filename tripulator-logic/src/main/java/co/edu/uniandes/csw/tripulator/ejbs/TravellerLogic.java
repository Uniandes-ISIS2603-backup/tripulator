package co.edu.uniandes.csw.tripulator.ejbs;

import co.edu.uniandes.csw.tripulator.entities.TravellerEntity;
import co.edu.uniandes.csw.tripulator.entities.TripEntity;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.tripulator.persistence.TravellerPersistence;
import co.edu.uniandes.csw.tripulator.persistence.TripPersistence;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import co.edu.uniandes.csw.tripulator.api.ITravellerLogic;
import java.util.ArrayList;

@Stateless
public class TravellerLogic implements ITravellerLogic {

    private static final Logger logger = Logger.getLogger(TravellerLogic.class.getName());

    @Inject
    private TravellerPersistence persistence;

    @Inject
    private TripPersistence tripPersistence;

    @Override
    public List<TravellerEntity> getTravellers() {
        logger.info("Getting all travellers.");
        List<TravellerEntity> travellers = persistence.findAll();
        logger.info("Finished getting all travellers.");
        return travellers;
    }

    @Override
    public TravellerEntity getTraveller(Long id) throws BusinessLogicException {
        logger.log(Level.INFO, "Getting traveller with id={0}", id);
        TravellerEntity traveller = persistence.find(id);
        if (traveller == null) {
            logger.log(Level.SEVERE, "Traveller with id={0} does not exist.", id);
            throw new BusinessLogicException("No such traveller with id={0}");
        }
        logger.log(Level.INFO, "Finish getting traveller with id={0}", id);
        return traveller;
    }

    @Override
    public TravellerEntity createTraveller(TravellerEntity entity) {
        logger.info("Creating new traveller");
        persistence.create(entity);
        logger.info("Finished creating new traveller");
        return entity;
    }

    @Override
    public TravellerEntity updateTraveller(TravellerEntity entity) {
        logger.log(Level.INFO, "Updating traveller with id={0}", entity.getId());
        TravellerEntity newEntity = persistence.update(entity);
        logger.log(Level.INFO, "Traveller with id={0} has been updated", entity.getId());
        return newEntity;
    }

    @Override
    public void deleteTraveller(Long id) {
        logger.log(Level.INFO, "Deleting traveller with id={0}", id);
        persistence.delete(id);
        logger.log(Level.INFO, "Traveller with id={0} has been deleted", id);
    }

    @Override
    public List<TripEntity> getTrips(Long idTraveller) {
        return persistence.find(idTraveller).getTrips();
    }

    @Override
    public TripEntity getTrip(Long idTraveller, Long idTrip) {
        List<TripEntity> trips = persistence.find(idTraveller).getTrips();
        TripEntity tripEntity = new TripEntity();
        tripEntity.setId(idTrip);
        int index = trips.indexOf(tripEntity);
        if (index >= 0) {
            return trips.get(index);
        }
        return null;
    }

    @Override
    public TripEntity addTrip(TripEntity trip, Long idTraveller) throws BusinessLogicException {
        TravellerEntity e = getTraveller(idTraveller);
        trip.setTraveller(e);
        e.addTrip(trip);
        trip = tripPersistence.create(trip);
        return trip;
    }

    @Override
    public void removeTrip(Long idTrip, Long idTraveller) {
        tripPersistence.delete(idTrip);
    }

    @Override
    public List<TripEntity> replaceTrips(List<TripEntity> trips, Long idTraveller) throws BusinessLogicException {
        List<TripEntity> oldTrips = tripPersistence.findAll(idTraveller);
        ArrayList<TripEntity> newTrips = new ArrayList<>();
        for (TripEntity trip : oldTrips) {
            tripPersistence.delete(trip.getId());
        }
        TravellerEntity traveller = getTraveller(idTraveller);

        for (TripEntity newTrip : trips) {
            newTrip.setTraveller(traveller);
            newTrip = tripPersistence.create(newTrip);
            newTrips.add(newTrip);
        }
        return newTrips;
    }
}
