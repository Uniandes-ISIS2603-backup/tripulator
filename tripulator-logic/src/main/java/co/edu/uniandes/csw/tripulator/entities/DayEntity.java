package co.edu.uniandes.csw.tripulator.entities;

import co.edu.uniandes.csw.crud.api.podam.strategy.DateStrategy;
import co.edu.uniandes.csw.crud.spi.entity.BaseEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Entity
public class DayEntity extends BaseEntity implements Serializable {
    
    @ManyToOne
    @PodamExclude
    private TripEntity trip;
    
    @OneToMany(mappedBy = "day")
    @PodamExclude
    private List<EventEntity> events = new ArrayList();
    
    @PodamStrategyValue(DateStrategy.class)
    @Temporal(TemporalType.DATE)
    private Date date;
    
    private String city;
    
    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param fecha the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }
    

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    public List<EventEntity> getEvents() {
        return events;
    }

    public void setEvents(List<EventEntity> events) {
        this.events = events;
    }
    
    public TripEntity getTrip() {
        return trip;
    }

    public void setTrip(TripEntity trip) {
        this.trip = trip;
    }
}
