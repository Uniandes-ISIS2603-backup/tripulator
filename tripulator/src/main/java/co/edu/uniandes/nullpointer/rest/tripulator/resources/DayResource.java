package co.edu.uniandes.nullpointer.rest.tripulator.resources;

import co.edu.uniandes.csw.tripulator.entities.DayEntity;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import co.edu.uniandes.nullpointer.rest.tripulator.converters.DayConverter;
import co.edu.uniandes.nullpointer.rest.tripulator.dtos.DayDTO;
import co.edu.uniandes.nullpointer.rest.tripulator.exceptions.TripulatorLogicException;
import java.util.List;
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
import co.edu.uniandes.csw.tripulator.api.IDayLogic;

@Path("/travellers/{idTraveller}/trips/{idTrip}/days")
@Produces("application/json")
@RequestScoped
public class DayResource {

    @Inject
    IDayLogic dayLogic;

    private final static Logger logger = Logger.getLogger(DayResource.class.getName());

    @GET
    public List<DayDTO> getDays(@PathParam("idTraveller") Long idTraveller,
            @PathParam("idTrip") Long idTrip) throws TripulatorLogicException, BusinessLogicException {
        List<DayDTO> days = DayConverter.listEntity2DTO(dayLogic.getDays(idTraveller,idTrip));
        logger.info("Viendo las ids:");
        for(DayDTO dia : days) logger.info(dia.toString());
        return days;
    }

    @GET
    @Path("{id: \\d+}")
    public DayDTO getDay(@PathParam("idTraveller") Long idTraveller,
            @PathParam("idTrip") Long idTrip,
            @PathParam("id") Long id) throws TripulatorLogicException, BusinessLogicException{
        return DayConverter.fullEntity2DTO(dayLogic.getDay(idTraveller, idTrip, id));
    }

    @POST
    public DayDTO createDay(@PathParam("idTraveller") Long idTraveller,
            @PathParam("idTrip") Long idTrip,
            DayDTO dia) throws TripulatorLogicException, BusinessLogicException {
        return DayConverter.fullEntity2DTO(dayLogic.createDay(idTraveller, idTrip, DayConverter.fullDTO2Entity(dia)));
    }

    @PUT
    @Path("{id: \\d+}")
    public DayDTO updateDay(@PathParam("idTraveller") Long idTraveller,
            @PathParam("idTrip") Long idTrip,
            @PathParam("id") Long id, DayDTO dia) throws TripulatorLogicException, BusinessLogicException {
        DayEntity converted = DayConverter.fullDTO2Entity(dia);
        return DayConverter.fullEntity2DTO(dayLogic.updateDay(idTraveller, idTrip, converted));
    }

    @DELETE
    @Path("{id: \\d+}")
    public void deleteDay(@PathParam("idTraveller") Long idTraveller,
            @PathParam("idTrip") Long idTrip,
            @PathParam("id") Long id) throws TripulatorLogicException, BusinessLogicException {
        dayLogic.deleteDay(idTraveller, idTrip, id);
    }
}
