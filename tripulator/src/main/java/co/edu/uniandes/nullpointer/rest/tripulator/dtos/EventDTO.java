/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.nullpointer.rest.tripulator.dtos;

import co.edu.uniandes.csw.crud.api.podam.strategy.DateStrategy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamStrategyValue;

/**
 *
 * @author Jose Daniel
 */
@XmlRootElement
public class EventDTO {

    private Long id;
    
    private String name;
    
    @PodamStrategyValue(DateStrategy.class)
    private Date arrivalDate;
    
    @PodamStrategyValue(DateStrategy.class)
    private Date departureDate;
    
    private String description;

    public EventDTO() {
    }

    public EventDTO(Long id, String name, Date start,
            Date end, String description) {
        super();
        this.id = id;
        this.name = name;
        this.arrivalDate = start;
        this.departureDate = end;
        this.description = description;
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Convierte el objeto a una cadena
     */
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:mm");
        return "{id:" + id 
                + ",\n name:'" + name
                + "',\n arrivalDate:" + sdf.format(arrivalDate)
                + ",\n  departureDate:" + sdf.format(departureDate)
                + ",\n description:'" + description + "'}";
    }

}
