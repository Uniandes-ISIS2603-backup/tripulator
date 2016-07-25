package co.edu.uniandes.nullpointer.rest.tripulator.resources;

import co.edu.uniandes.csw.tripulator.entities.TripEntity;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import co.edu.uniandes.nullpointer.rest.tripulator.converters.TripConverter;
import co.edu.uniandes.nullpointer.rest.tripulator.dtos.TripDTO;
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
import co.edu.uniandes.csw.tripulator.api.ITripLogic;

@Path("/travellers/{idTraveller}/trips")
@Produces("application/json")
@RequestScoped
public class TripResource {

    @Inject
    ITripLogic tripLogic;

    private final static Logger logger = Logger.getLogger(TripResource.class.getName());

    /**
     * Get all trips.
     * @param idTraveller
     * @return trips
     * @throws TripulatorLogicException
     */
    @GET
    public List<TripDTO> getTrips(@PathParam("idTraveller") Long idTraveller) throws TripulatorLogicException, BusinessLogicException {
        return TripConverter.listEntity2DTO(tripLogic.getTrips(idTraveller));
    }

    /**
     * Gets a specific trip.
     * @param idTraveller
     * @param id trip identifier.
     * @return trip.
     * @throws
     * co.edu.uniandes.nullpointer.rest.tripulator.exceptions.TripulatorLogicException
     * @throws co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException
     */
    @GET
    @Path("{id: \\d+}")
    public TripDTO getTrip(@PathParam("idTraveller") Long idTraveller,
            @PathParam("id") Long id) throws TripulatorLogicException, BusinessLogicException {
        return TripConverter.fullEntity2DTO(tripLogic.getTrip(idTraveller, id));
    }

    /**
     * Adds a trip
     * @param idTraveller
     * @param trip to add.
     * @return Data of the trip that was added
     * @throws
     * co.edu.uniandes.nullpointer.rest.tripulator.exceptions.TripulatorLogicException
     */
    @POST
    public TripDTO createTrip(@PathParam("idTraveller") Long idTraveller,
            TripDTO trip) throws TripulatorLogicException, BusinessLogicException {
        return TripConverter.fullEntity2DTO(tripLogic.createTrip(idTraveller, TripConverter.fullDTO2Entity(trip)));
    }

    /**
     * Updates a trip's data.
     * @param idTraveller
     * @param trip trip a modificar
     * @return datos de la viajero modificada
     * @throws TripulatorLogicException cuando no existe un trip con el id
     * suministrado
     */
    @PUT
    @Path("{id: \\d+}")
    public TripDTO updateTrip(@PathParam("idTraveller") Long idTraveller,
            TripDTO trip) throws TripulatorLogicException, BusinessLogicException {
        TripEntity iConverted = TripConverter.fullDTO2Entity(trip);
        return TripConverter.fullEntity2DTO(tripLogic.updateTrip(idTraveller, iConverted));
    }

    /**
     * Deletes all data of the trip.
     * @param idTraveller
     * @param id identifier for the trip that is going to be deleted.
     * @throws TripulatorLogicException no trip with id.
     */
    @DELETE
    @Path("{id: \\d+}")
    public void deleteTrip(@PathParam("idTraveller") Long idTraveller,
            @PathParam("id") Long id) throws TripulatorLogicException, BusinessLogicException {
        tripLogic.deleteTrip(idTraveller, id);
    }
}
