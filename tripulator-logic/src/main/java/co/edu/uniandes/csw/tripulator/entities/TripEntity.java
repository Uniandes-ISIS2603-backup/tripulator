package co.edu.uniandes.csw.tripulator.entities;

import co.edu.uniandes.csw.crud.api.podam.strategy.DateStrategy;
import co.edu.uniandes.csw.crud.spi.entity.BaseEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamStrategyValue;


@Entity
public class TripEntity extends BaseEntity implements Serializable {
    @PodamStrategyValue(DateStrategy.class)
    @Temporal(TemporalType.DATE)
    private Date arrivalDate;
    
    @PodamStrategyValue(DateStrategy.class)
    @Temporal(TemporalType.DATE)
    private Date departureDate;
    
    private String country;
    
    @ManyToOne
    @PodamExclude
    private TravellerEntity traveller;
    
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    @PodamExclude
    private List<DayEntity> days = new ArrayList<>();

    /**
     * @return the arrivalDate
     */
    public Date getArrivalDate() {
        return arrivalDate;
    }

    /**
     * @param arrivalDate the arrivalDate to set
     */
    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    /**
     * @return the departureDate
     */
    public Date getDepartureDate() {
        return departureDate;
    }

    /**
     * @param departureDate the departureDate to set
     */
    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    /**
     * @return the traveller
     */
    public TravellerEntity getTraveller() {
        return traveller;
    }

    /**
     * @param traveller the traveller to set
     */
    public void setTraveller(TravellerEntity traveller) {
        this.traveller = traveller;
    }

    /**
     * @return the days
     */
    public List<DayEntity> getDays() {
        return days;
    }

    /**
     * @param days the days to set
     */
    public void setDays(List<DayEntity> days) {
        this.days = days;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }
    
}
