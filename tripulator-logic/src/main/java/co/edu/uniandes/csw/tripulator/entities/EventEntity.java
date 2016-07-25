package co.edu.uniandes.csw.tripulator.entities;

import co.edu.uniandes.csw.crud.api.podam.strategy.DateStrategy;
import co.edu.uniandes.csw.crud.spi.entity.BaseEntity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Entity
public class EventEntity extends BaseEntity implements Serializable {

    @PodamStrategyValue(DateStrategy.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrivalDate;
    
    @PodamStrategyValue(DateStrategy.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date departureDate;
    
    private String description;

    @ManyToOne
    @PodamExclude
    private DayEntity day;

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DayEntity getDay() {
        return day;
    }

    public void setDay(DayEntity day) {
        this.day = day;
    }
}
