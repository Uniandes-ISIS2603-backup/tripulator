package co.edu.uniandes.csw.tripulator.api;
import co.edu.uniandes.csw.tripulator.entities.DayEntity;
import co.edu.uniandes.csw.tripulator.entities.EventEntity;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import java.util.List;

public interface IDayLogic {

    public List<DayEntity> getDays(Long idTraveller, Long idTrip) throws BusinessLogicException;

    public DayEntity getDay(Long idTraveller, Long idTrip, Long idDay) throws BusinessLogicException;

    public DayEntity createDay(Long idTraveller, Long idTrip, DayEntity day) throws BusinessLogicException;

    public DayEntity updateDay(Long idTraveller, Long idTrip, DayEntity day) throws BusinessLogicException;

    public void deleteDay(Long idTraveller, Long idTrip, Long idDay) throws BusinessLogicException;
    
    public List<EventEntity> getEvents(Long idTraveller, Long idTrip, Long idDay);
    
    public EventEntity getEvent(Long idTraveller, Long idTrip, Long idDay, Long idEvent);
    
    public EventEntity addEvent(Long idTraveller, Long idTrip, Long idDay, EventEntity event);
    
    public void removeEvent(Long idTraveller, Long idTrip, Long idDay, Long idEvent);
   
    public List<EventEntity> replaceEvents(Long idTraveller, Long idTrip, Long idDay, List<EventEntity>events);
}
