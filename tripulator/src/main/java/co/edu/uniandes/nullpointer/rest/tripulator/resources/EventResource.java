package co.edu.uniandes.nullpointer.rest.tripulator.resources;

import co.edu.uniandes.csw.tripulator.entities.EventEntity;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import co.edu.uniandes.nullpointer.rest.tripulator.converters.EventConverter;
import co.edu.uniandes.nullpointer.rest.tripulator.dtos.EventDTO;
import co.edu.uniandes.nullpointer.rest.tripulator.exceptions.TripulatorLogicException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import co.edu.uniandes.csw.tripulator.api.IDayLogic;
import co.edu.uniandes.csw.tripulator.api.IEventLogic;

/**
 *
 * @author jd.fandino10
 */
@Path("/travellers/{idTraveller}/trips/{idTrip}/days/{idDay}/events")
@Produces("application/json")
@RequestScoped
public class EventResource {

    private static final Logger LOGGER = Logger.getLogger(EventResource.class.getName());

    @Inject
    IEventLogic eventLogic;

    @Inject
    IDayLogic dayLogic;

    @GET
    public List<EventDTO> getEvents() throws TripulatorLogicException {
        return EventConverter.listEntity2DTO(eventLogic.getEvents());
    }

    @GET
    @Path("{id: \\d+}")
    public EventDTO getEvent(@PathParam("id") Long id) throws BusinessLogicException {
        return EventConverter.fullEntity2DTO(eventLogic.getEvento(id));
    }

    @POST
    public EventDTO createEvent(EventDTO dto) throws TripulatorLogicException {
        EventEntity entity = EventConverter.fullDTO2Entity(dto);
        EventEntity newEntity;
        try {
            newEntity = eventLogic.createEvento(entity);
        } catch (BusinessLogicException ex) {
            LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            throw new WebApplicationException(ex.getLocalizedMessage(), ex, Response.Status.BAD_REQUEST);
        }
        return EventConverter.fullEntity2DTO(newEntity);
    }

    @PUT
    @Path("{id: \\d+}")
    public EventDTO updateEvent(@PathParam("id") Long id, EventDTO dto) throws TripulatorLogicException {
        LOGGER.log(Level.INFO, "Se ejecuta método updateEvento con id={0}", id);
        EventEntity entity = EventConverter.fullDTO2Entity(dto);
        entity.setId(id);
        try {
            EventEntity savedEvento = eventLogic.updateEvento(entity);
            return EventConverter.fullEntity2DTO(savedEvento);
        } catch (BusinessLogicException ex) {
            LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            throw new WebApplicationException(ex.getLocalizedMessage(), ex, Response.Status.BAD_REQUEST);
        }
    }

    @DELETE
    @Path("{id: \\d+}")
    public void deleteEvento(@PathParam("id") Long id) throws TripulatorLogicException {
        eventLogic.deleteEvento(id);
    }
}
