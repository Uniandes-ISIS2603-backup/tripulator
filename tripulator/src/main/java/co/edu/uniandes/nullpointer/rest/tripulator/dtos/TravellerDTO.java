package co.edu.uniandes.nullpointer.rest.tripulator.dtos;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import uk.co.jemos.podam.common.PodamExclude;

@XmlRootElement
public class TravellerDTO {
    private Long id;
    private String password;
    private String user;
    
    @PodamExclude
    private List<TripDTO> trips = new ArrayList<>();

    public TravellerDTO(){
        
    }
    
    public TravellerDTO(Long id, String password, String user) {
        super();
        this.id = id;
        this.password = password;
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }

    public List<TripDTO> getTrips() {
        return trips;
    }

    public void setTrips(List<TripDTO> trips) {
        this.trips = trips;
    }

    @Override
    public String toString() {
        return "{ id : " + id
                + ", password : \"" +  password + "\" "
                + ", user : \"" + user + "\" " + "}";
    }
}
