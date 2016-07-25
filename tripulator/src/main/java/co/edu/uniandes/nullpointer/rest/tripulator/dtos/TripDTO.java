package co.edu.uniandes.nullpointer.rest.tripulator.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TripDTO {

    private Long id;
    private String name;
    private String country;
    private Date arrivalDate;
    private Date departureDate;
    private List<DayDTO> days;

    public TripDTO() {

    }

    public TripDTO(Long id, String name, String country, Date arrivalDate, Date departureDate, List<DayDTO> days) {
        super();
        this.id = id;
        this.name = name;
        this.country = country;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.days = days;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

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
     * @return the days
     */
    public List<DayDTO> getDays() {
        return days;
    }

    /**
     * @param days the days to set
     */
    public void setDays(List<DayDTO> days) {
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

    @Override
    public String toString() {
        return "{id:" + id 
                + ",\n name:'" + name 
                + "',\n country:'" + country
                + "',\n arrivalDate:'" + arrivalDate
                + "', \n departureDate:'" + departureDate
                + "',\n days:" + days + " }";
    }
}
