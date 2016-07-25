package co.edu.uniandes.nullpointer.rest.tripulator.resources;

import co.edu.uniandes.csw.tripulator.entities.TravellerEntity;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import co.edu.uniandes.nullpointer.rest.tripulator.converters.TravellerConverter;
import co.edu.uniandes.nullpointer.rest.tripulator.dtos.TravellerDTO;
import co.edu.uniandes.nullpointer.rest.tripulator.exceptions.TripulatorLogicException;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import co.edu.uniandes.csw.tripulator.api.ITravellerLogic;

@Path("/travellers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TravellerResource {

    private static final Logger logger = Logger.getLogger(TravellerResource.class.getName());
    @Inject
    ITravellerLogic travellerLogic;

    @GET
    public List<TravellerDTO> getTravellers() throws TripulatorLogicException {
        return TravellerConverter.listEntity2DTO(travellerLogic.getTravellers());
    }

    /**
     * Obtiene un viajero
     *
     * @param id identificador del viajero.
     * @return viajero encontrado.
     * @throws
     * co.edu.uniandes.nullpointer.rest.tripulator.exceptions.TripulatorLogicException
     * TripulatorLogicException cuando el viajero no existe
     */
    @GET
    @Path("{id: \\d+}")
    public TravellerDTO getTraveller(@PathParam("id") Long id) throws TripulatorLogicException, BusinessLogicException {
        return TravellerConverter.fullEntity2DTO(travellerLogic.getTraveller(id));
    }

    /**
     * Agrega un viajero
     *
     * @param viajero viajerp a agregar
     * @return datos del viajero a agregar
     * @throws
     * co.edu.uniandes.nullpointer.rest.tripulator.exceptions.TripulatorLogicException
     */
    @POST
    public TravellerDTO createTraveller(TravellerDTO viajero) throws TripulatorLogicException {
        TravellerEntity entity = TravellerConverter.fullDTO2Entity(viajero);
        return TravellerConverter.fullEntity2DTO(travellerLogic.createTraveller(entity));
    }

    @PUT
    @Path("{id: \\d+}")
    public TravellerDTO updateTraveller(@PathParam("id") Long id, TravellerDTO dto) throws BusinessLogicException {
        TravellerEntity entity = TravellerConverter.fullDTO2Entity(dto);
        entity.setId(id);
        //ViajeroEntity oldEntity = travellerLogic.getTraveller(id);
        //entity.setItinerarios(oldEntity.getTrips());
        return TravellerConverter.fullEntity2DTO(travellerLogic.updateTraveller(entity));
    }

    /**
     * Elimina un objeto de Itinerario de la base de datos.
     *
     * @param id Identificador del objeto a eliminar.
     * @generated
     */
    @DELETE
    @Path("{id: \\d+}")
    public void deleteTraveller(@PathParam("id") Long id) {
        travellerLogic.deleteTraveller(id);
    }
}
