package co.edu.uniandes.csw.tripulator.ejbs;

import co.edu.uniandes.csw.tripulator.entities.DayEntity;
import co.edu.uniandes.csw.tripulator.entities.EventEntity;
import co.edu.uniandes.csw.tripulator.entities.TripEntity;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.tripulator.persistence.DayPersistence;
import co.edu.uniandes.csw.tripulator.persistence.EventPersistence;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import co.edu.uniandes.csw.tripulator.api.ITripLogic;
import co.edu.uniandes.csw.tripulator.api.IDayLogic;

@Stateless
public class DayLogic implements IDayLogic {
    
    private static final Logger logger = Logger.getLogger(DayLogic.class.getName());
    
    @Inject
    private DayPersistence persistence;
    
    @Inject
    private ITripLogic tripLogic;
    
    @Inject EventPersistence eventPersistence;
    
    @Override
    public List<DayEntity> getDays(Long idTraveller, Long idTrip) throws BusinessLogicException{
        logger.info("Getting all days of trip with id= "+ idTrip);
        TripEntity trip = tripLogic.getTrip(idTraveller, idTrip);
        logger.info("Finished getting all days of trip with id=" + idTrip);
        return trip.getDays();
    }

    @Override
    public DayEntity getDay(Long idTraveller, Long idTrip, Long id) throws BusinessLogicException {
        logger.log(Level.INFO, "Getting day with id={0}", id);
        DayEntity day = persistence.find(idTrip, id);
        if (day == null) {
            logger.log(Level.SEVERE, "Day with id= {0} does not exist", id);
            throw new BusinessLogicException("The day does not exist");
        }
        logger.log(Level.INFO, "Finished getting day with id={0}", id);
        return day;
    }

    @Override
    public DayEntity createDay(Long idTraveller, Long idTrip, DayEntity entity) throws BusinessLogicException {
        TripEntity trip = tripLogic.getTrip(idTraveller, idTrip);
        logger.info("Creating day with id " + entity.getId());
        entity.setTrip(trip);
        persistence.create(entity);
        logger.info("Finished creating day with id: " + entity.getId());
        return entity;
    }

    @Override
    public DayEntity updateDay(Long idTraveller, Long idTrip, DayEntity entity) throws BusinessLogicException{
        logger.log(Level.INFO, "Updating day with id={0}", entity.getId());
        TripEntity trip = tripLogic.getTrip(idTraveller, idTrip);
        entity.setTrip(trip);
        DayEntity newEntity = persistence.update(entity);
        logger.log(Level.INFO, "Finished updating day with id={0}", entity.getId());
        return newEntity;
    }

    @Override
    public void deleteDay(Long idTraveller, Long idTrip, Long id) throws BusinessLogicException{
        logger.log(Level.INFO, "Deleting day with id={0}", id);
        DayEntity old = getDay(idTraveller, idTrip, id);
        persistence.delete(old.getId());
        logger.log(Level.INFO, "Finished deleting day with id={0}", id);
    }

    @Override
    public List<EventEntity> getEvents(Long idTraveller, Long idTrip, Long idDay) {
        return persistence.find(idTrip, idDay).getEvents();
    }

    @Override
    public EventEntity getEvent(Long idTraveller, Long idTrip, Long idDay, Long idEvent) {
        List<EventEntity> events = persistence.find(idTrip, idDay).getEvents();
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(idEvent);
        int index = events.indexOf(eventEntity);
        if (index >= 0) {
            return events.get(index);
        }
        return null;
    }

    @Override
    public EventEntity addEvent(Long idTraveller, Long idTrip, Long idDay, EventEntity event) {
        DayEntity day = persistence.find(idTrip, idDay);
        event.setDay(day);
        eventPersistence.create(event);
        return event;
    }

    @Override
    public void removeEvent(Long idTraveller, Long idTrip, Long idDay, Long idEvento) {
        eventPersistence.delete(idEvento);
    }

    @Override
    public List<EventEntity> replaceEvents(Long idTraveller, Long idTrip, Long idDay, List<EventEntity>events) {
        List<EventEntity> oldEvents = eventPersistence.findAll(idDay);
        for (EventEntity event : oldEvents) {
            eventPersistence.delete(event.getId());
        }
        DayEntity day = persistence.find(idTrip, idDay);
        for (EventEntity event: events){
            event.setDay(day);
            eventPersistence.create(event);
        }
        return events;
    }
}
