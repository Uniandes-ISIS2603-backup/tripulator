package co.edu.uniandes.csw.tripulator.api;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.tripulator.entities.EventEntity;
import java.util.List;

public interface IEventLogic {
    public List<EventEntity> getEvents(Long idTraveller, Long idTrip, Long idDay);

    public EventEntity getEvent(Long idTraveller, Long idTrip, Long idDay, Long idEvent) throws BusinessLogicException;

    public EventEntity createEvent(Long idTraveller, Long idTrip, Long idDay, EventEntity entity) throws BusinessLogicException;

    public EventEntity updateEvent(Long idTraveller, Long idTrip, Long idDay, EventEntity entity) throws BusinessLogicException;

    public void deleteEvent(Long idTraveller, Long idTrip, Long idDay,Long idEvent) throws BusinessLogicException;
}
