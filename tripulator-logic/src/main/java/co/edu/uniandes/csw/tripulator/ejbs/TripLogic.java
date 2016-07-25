package co.edu.uniandes.csw.tripulator.ejbs;

import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.tripulator.entities.DayEntity;
import co.edu.uniandes.csw.tripulator.entities.TripEntity;
import co.edu.uniandes.csw.tripulator.entities.TravellerEntity;
import co.edu.uniandes.csw.tripulator.persistence.DayPersistence;
import co.edu.uniandes.csw.tripulator.persistence.TripPersistence;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import co.edu.uniandes.csw.tripulator.api.ITravellerLogic;
import co.edu.uniandes.csw.tripulator.api.ITripLogic;

@Stateless
public class TripLogic implements ITripLogic {

    private static final Logger logger = Logger.getLogger(TripLogic.class.getName());

    @Inject
    private ITravellerLogic travellerLogic;

    @Inject
    private TripPersistence tripPersistence;

    @Inject
    private DayPersistence dayPersistence;

    @Override
    public List<TripEntity> getTrips(Long idTraveller) throws BusinessLogicException {
        logger.info("Getting all trips.");
        TravellerEntity traveller = travellerLogic.getTraveller(idTraveller);
        logger.info("Finished getting all trips.");
        return traveller.getTrips();
    }

    @Override
    public TripEntity getTrip(Long idTraveller, Long idTrip) throws BusinessLogicException {
        logger.log(Level.INFO, "Getting trip with id={0}", idTrip);
        TripEntity trip = tripPersistence.find(idTraveller, idTrip);
        if (trip == null) {
            logger.log(Level.SEVERE, "Trip with id {0} does not exist", idTrip);
            throw new BusinessLogicException("The trip does not exist");
        }
        logger.log(Level.INFO, "Finished getting trip with id={0}", idTrip);
        return trip;
    }

    @Override
    public TripEntity createTrip(Long idTraveller, TripEntity entity) throws BusinessLogicException {
        TravellerEntity traveller = travellerLogic.getTraveller(idTraveller);
        if (traveller == null) {
            throw new IllegalArgumentException("The traveller does not exist");
        }
        logger.info("Creating new trip");
        logger.info("Found traveller with id: " + traveller.getId());
        entity.setTraveller(traveller);
        entity = tripPersistence.create(entity);
        logger.info("Finished creating trip");
        return entity;
    }

    @Override
    public TripEntity updateTrip(Long idTraveller, TripEntity entity) throws BusinessLogicException {
        logger.log(Level.INFO, "Inicia proceso de actualizar trip con id={0}", entity.getId());
        TravellerEntity traveller = travellerLogic.getTraveller(idTraveller);
        entity.setTraveller(traveller);
        TripEntity newEntity = tripPersistence.update(entity);
        logger.log(Level.INFO, "Termina proceso de actualizar trip con id={0}", entity.getId());
        return newEntity;
    }

    @Override
    public void deleteTrip(Long idTraveller, Long idTrip) throws BusinessLogicException {
        logger.log(Level.INFO, "Deleting trip with id={0}", idTrip);
        TripEntity oldTrip = getTrip(idTraveller, idTrip);
        tripPersistence.delete(oldTrip.getId());
        logger.log(Level.INFO, "Finished deleting trip with id={0}", idTrip);
    }

    @Override
    public List<DayEntity> getDays(Long idTraveller, Long idTrip) {
        return tripPersistence.find(idTraveller, idTrip).getDays();
    }

    @Override
    public DayEntity getDay(Long idTraveller, Long idTrip, Long dayId) {
        List<DayEntity> days = tripPersistence.find(idTraveller, idTrip).getDays();
        DayEntity dayEntity = new DayEntity();
        dayEntity.setId(dayId);
        int index = days.indexOf(dayEntity);
        if (index >= 0) {
            return days.get(index);
        }
        return null;
    }

    @Override
    public List<DayEntity> replaceDays(List<DayEntity> days, Long idTraveller, Long idTrip) throws BusinessLogicException {
        List<DayEntity> oldDays = dayPersistence.findAll(idTrip);
        for (DayEntity day : oldDays) {
            dayPersistence.delete(day.getId());
        }
        TripEntity trip = getTrip(idTraveller, idTrip);
        for (DayEntity newDay : days) {
            newDay.setTrip(trip);
            dayPersistence.create(newDay);
        }
        return days;
    }

    @Override
    public void removeDay(Long idTraveller, Long idTrip, Long idDay) throws BusinessLogicException {
        dayPersistence.delete(idDay);
    }

    @Override
    public DayEntity addDay(Long idTraveller, Long idTrip, DayEntity day) throws BusinessLogicException {
        TripEntity trip = getTrip(idTraveller, idTrip);
        day.setTrip(trip);
        dayPersistence.create(day);
        return day;
    }
}
