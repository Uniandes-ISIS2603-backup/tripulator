/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.nullpointer.rest.tripulator.converters;

import co.edu.uniandes.csw.tripulator.entities.EventEntity;
import co.edu.uniandes.nullpointer.rest.tripulator.dtos.EventDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventConverter {

    private EventConverter() {
    }

    public static EventDTO refEntity2DTO(EventEntity entity) {
        if (entity != null) {
            EventDTO dto = new EventDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setArrivalDate(entity.getArrivalDate());
            dto.setDepartureDate(entity.getDepartureDate());
            dto.setDescription(entity.getDescription());
            return dto;
        } else {
            return null;
        }
    }

    public static EventEntity refDTO2Entity(EventDTO dto) {
        if (dto != null) {
            EventEntity entity = new EventEntity();
            entity.setId(dto.getId());

            return entity;
        } else {
            return null;
        }
    }

    private static EventDTO basicEntity2DTO(EventEntity entity) {
        if (entity != null) {
            EventDTO dto = new EventDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setArrivalDate(entity.getArrivalDate());
            dto.setDepartureDate(entity.getDepartureDate());
            dto.setDescription(entity.getDescription());
            return dto;
        } else {
            return null;
        }
    }

    private static EventEntity basicDTO2Entity(EventDTO dto) {
        if (dto != null) {
            EventEntity entity = new EventEntity();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setArrivalDate(dto.getArrivalDate());
            entity.setDepartureDate(dto.getDepartureDate());
            entity.setDescription(dto.getDescription());
            return entity;
        } else {
            return null;
        }
    }

    public static EventDTO fullEntity2DTO(EventEntity entity) {
        if (entity != null) {
            EventDTO dto = basicEntity2DTO(entity);
            return dto;
        } else {
            return null;
        }
    }

    public static EventEntity fullDTO2Entity(EventDTO dto) {
        if (dto != null) {
            EventEntity entity = basicDTO2Entity(dto);
            return entity;
        } else {
            return null;
        }
    }

    public static List<EventDTO> listEntity2DTO(List<EventEntity> entities) {
        List<EventDTO> dtos = new ArrayList<EventDTO>();
        if (entities != null) {
            for (EventEntity entity : entities) {
                dtos.add(basicEntity2DTO(entity));
            }
        }
        return dtos;
    }

    public static List<EventEntity> listDTO2Entity(List<EventDTO> dtos) {
        List<EventEntity> entities = new ArrayList<EventEntity>();
        if (dtos != null) {
            for (EventDTO dto : dtos) {
                entities.add(basicDTO2Entity(dto));
            }
        }
        return entities;
    }
}
