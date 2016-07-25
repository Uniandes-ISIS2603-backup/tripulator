package co.edu.uniandes.nullpointer.rest.tripulator.converters;

import co.edu.uniandes.csw.tripulator.entities.TripEntity;
import co.edu.uniandes.nullpointer.rest.tripulator.dtos.TripDTO;
import java.util.ArrayList;
import java.util.List;

public class TripConverter {

    private TripConverter() {
    }

    public static TripDTO refEntity2DTO(TripEntity entity) {
        if (entity != null) {
            TripDTO dto = new TripDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setArrivalDate(entity.getArrivalDate());
            dto.setDepartureDate(entity.getDepartureDate());
            dto.setCountry(entity.getCountry());
            return dto;
        } else {
            return null;
        }
    }

    public static TripEntity refDTO2Entity(TripDTO dto) {
        if (dto != null) {
            TripEntity entity = new TripEntity();
            entity.setId(dto.getId());

            return entity;
        } else {
            return null;
        }
    }

    private static TripDTO basicEntity2DTO(TripEntity entity) {
        if (entity != null) {
            TripDTO dto = new TripDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setArrivalDate(entity.getArrivalDate());
            dto.setDepartureDate(entity.getDepartureDate());
            dto.setCountry(entity.getCountry());
            return dto;
        } else {
            return null;
        }
    }

    private static TripEntity basicDTO2Entity(TripDTO dto) {
        if (dto != null) {
            TripEntity entity = new TripEntity();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setArrivalDate(dto.getArrivalDate());
            entity.setDepartureDate(dto.getDepartureDate());
            entity.setCountry(dto.getCountry());
            return entity;
        } else {
            return null;
        }
    }

    public static TripDTO fullEntity2DTO(TripEntity entity) {
        if (entity != null) {
            TripDTO dto = basicEntity2DTO(entity);
            return dto;
        } else {
            return null;
        }
    }

    public static TripEntity fullDTO2Entity(TripDTO dto) {
        if (dto != null) {
            TripEntity entity = basicDTO2Entity(dto);
            return entity;
        } else {
            return null;
        }
    }

    public static List<TripDTO> listEntity2DTO(List<TripEntity> entities) {
        List<TripDTO> dtos = new ArrayList<TripDTO>();
        if (entities != null) {
            for (TripEntity entity : entities) {
                dtos.add(basicEntity2DTO(entity));
            }
        }
        return dtos;
    }

    public static List<TripEntity> listDTO2Entity(List<TripDTO> dtos) {
        List<TripEntity> entities = new ArrayList<TripEntity>();
        if (dtos != null) {
            for (TripDTO dto : dtos) {
                entities.add(basicDTO2Entity(dto));
            }
        }
        return entities;
    }
}
