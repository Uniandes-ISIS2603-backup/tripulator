package co.edu.uniandes.csw.tripulator.entities;

import co.edu.uniandes.csw.crud.spi.entity.BaseEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.*;
import uk.co.jemos.podam.common.PodamExclude;

@Entity
public class TravellerEntity extends BaseEntity implements Serializable {
    private String user;
    private String password;
    
    @OneToMany(mappedBy = "traveller", cascade = CascadeType.ALL, orphanRemoval = true)
    @PodamExclude
    private List<TripEntity> trips = new ArrayList<>();

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<TripEntity> getTrips() {
        return trips;
    }

    public void setTrips(List<TripEntity> trips) {
        this.trips = trips;
    }
    
    public void addTrip(TripEntity trip){
        trips.add(trip);
    }
    
    public void removeTrip(TripEntity trip){
        trips.remove(trip);
    }

}