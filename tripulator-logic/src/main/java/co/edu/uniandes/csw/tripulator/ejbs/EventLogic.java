package co.edu.uniandes.csw.tripulator.ejbs;

import co.edu.uniandes.csw.tripulator.api.IDayLogic;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.tripulator.entities.DayEntity;
import co.edu.uniandes.csw.tripulator.entities.EventEntity;
import co.edu.uniandes.csw.tripulator.persistence.EventPersistence;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import co.edu.uniandes.csw.tripulator.api.IEventLogic;

@Stateless
public class EventLogic implements IEventLogic {

    private static final Logger LOGGER = Logger.getLogger(EventLogic.class.getName());

    @Inject
    private EventPersistence persistence;

    @Inject
    private IDayLogic dayLogic;

    @Override
    public List<EventEntity> getEvents(Long idTraveller, Long idTrip, Long idDay) {
        LOGGER.info("Getting all events from day " + idDay);
        List<EventEntity> events = persistence.findAll(idDay);
        LOGGER.info("Finished getting all events");
        return events;
    }


    @Override
    public EventEntity getEvent(Long idTraveller, Long idTrip, Long idDay, Long idEvent) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Getting event with id={0}", idEvent);
        EventEntity event = persistence.find(idDay, idEvent);
        if (event == null) {
            LOGGER.log(Level.SEVERE, "Event with id {0} does not exist", idEvent);
            throw new BusinessLogicException("Event does not exist");
        }
        LOGGER.log(Level.INFO, "Finished getting event with id={0}", idEvent);
        return event;
    }

    @Override
    public EventEntity createEvent(Long idTraveller, Long idTrip, Long idDay, EventEntity entity) throws BusinessLogicException {
        DayEntity day = dayLogic.getDay(idTraveller, idTrip, idDay);
        LOGGER.info("Creating event");
        entity.setDay(day);   
        if (entity.getArrivalDate().after(entity.getDepartureDate())) {
            throw new BusinessLogicException("Arrival date must be < than departure date");
        }
        persistence.create(entity);
        LOGGER.info("Finished creating event");
        return entity;
    }

    @Override
    public EventEntity updateEvent(Long idTraveller, Long idTrip, Long idDay, EventEntity entity) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Updating event with id={0}", entity.getId());
        if (entity.getArrivalDate().after(entity.getDepartureDate())) {
            throw new BusinessLogicException("Arrival date must be before departure date.");
        }
        DayEntity day = dayLogic.getDay(idTraveller, idTrip, idDay);
        entity.setDay(day);
        EventEntity newEntity = persistence.update(entity);
        LOGGER.log(Level.INFO, "Finished updating event with id={0}", entity.getId());
        return newEntity;
    }

    @Override
    public void deleteEvent(Long idTraveller, Long idTrip, Long idDay, Long id) throws BusinessLogicException{
        LOGGER.log(Level.INFO, "Deleting event with id={0}", id);
        EventEntity old = getEvent(idTraveller, idTrip, idDay, id);
        persistence.delete(old.getId());
        LOGGER.log(Level.INFO, "Finished deleting event with id={0}", id);
    }
}
