package co.edu.uniandes.csw.tripulator.api;

import co.edu.uniandes.csw.tripulator.entities.TravellerEntity;
import co.edu.uniandes.csw.tripulator.entities.TripEntity;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import java.util.List;

public interface ITravellerLogic {

    public List<TravellerEntity> getTravellers();

    public TravellerEntity getTraveller(Long idTraveller) throws BusinessLogicException;

    public TravellerEntity createTraveller(TravellerEntity entity);

    public TravellerEntity updateTraveller(TravellerEntity entity);

    public void deleteTraveller(Long idTraveller);

    public TripEntity addTrip(TripEntity trip, Long idTraveller)throws BusinessLogicException;

    public void removeTrip(Long idTrip, Long idTraveller);

    public List<TripEntity> replaceTrips(List<TripEntity> trips, Long idTraveller) throws BusinessLogicException;

    public List<TripEntity> getTrips(Long idTraveller);

    public TripEntity getTrip(Long idTrip, Long idTraveller);
}
