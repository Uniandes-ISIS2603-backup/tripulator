/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.nullpointer.rest.tripulator.dtos;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

public class DayDTO {

    private Long id;
    private Date date;
    private List<EventDTO> events;
    private String city;

    public DayDTO() {
    }

    public DayDTO(Long id, Date date, List<EventDTO> events, String city) {
        super();
        this.id = id;
        this.date = date;
        this.events = events;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    /**
     * Convierte el objeto a una cadena
     */
    @Override
    public String toString() {
        return "{ \n id : " + id + ",\n date : \"" + date + "\" "
                + ",\n events : \"" + events + "\" "
                + ",\n city : \"" + city + "\"" + "}";

    }
}
