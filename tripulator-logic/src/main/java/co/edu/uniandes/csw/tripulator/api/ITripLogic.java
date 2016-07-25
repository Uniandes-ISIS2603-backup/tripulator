/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.tripulator.api;

import co.edu.uniandes.csw.tripulator.entities.DayEntity;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.tripulator.entities.TripEntity;
import java.util.List;

/**
 *
 * @author Antonio de la Vega
 */
public interface ITripLogic {
    
    public List<TripEntity> getTrips(Long idTraveller)throws BusinessLogicException;
    
    public TripEntity getTrip(Long idTraveller, Long idTrip)throws BusinessLogicException;
    
    public TripEntity createTrip(Long idTraveller, TripEntity entity)throws BusinessLogicException; 
    
    public TripEntity updateTrip(Long idTraveller, TripEntity entity)throws BusinessLogicException;
    
    public void deleteTrip(Long idTraveller, Long idTrip) throws BusinessLogicException;
    
    public List<DayEntity> replaceDays(List<DayEntity> days, Long idTraveller, Long idTrip) throws BusinessLogicException;
        
    public DayEntity getDay(Long idTraveller, Long idTrip, Long idDay);
        
    public List<DayEntity> getDays(Long idTraveller, Long idTrip);    
    
    public void removeDay(Long idTraveller, Long idTrip, Long idDay) throws BusinessLogicException;
    
    public DayEntity addDay(Long idTraveller, Long idTrip, DayEntity day) throws BusinessLogicException;

}
