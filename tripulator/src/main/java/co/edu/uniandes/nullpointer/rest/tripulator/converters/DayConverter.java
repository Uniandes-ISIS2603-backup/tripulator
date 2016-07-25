package co.edu.uniandes.nullpointer.rest.tripulator.converters;

import co.edu.uniandes.csw.tripulator.entities.DayEntity;
import co.edu.uniandes.nullpointer.rest.tripulator.dtos.DayDTO;
import java.util.ArrayList;
import java.util.List;

public class DayConverter {

    private DayConverter() {
    }
    
    public static DayDTO refEntity2DTO(DayEntity entity) {
        if (entity != null) {
            DayDTO dto = new DayDTO();
            dto.setId(entity.getId());
            dto.setDate(entity.getDate());
            dto.setCity(entity.getCity());
            return dto;
        } 
        else {
            return null;
        }
    }
    
    public static DayEntity refDTO2Entity(DayDTO dto) {
        if (dto != null) {
            DayEntity entity = new DayEntity();
            entity.setId(dto.getId());

            return entity;
        } 
        else {
            return null;
        }
    }

    private static DayDTO basicEntity2DTO(DayEntity entity) {
        if (entity != null) {
            DayDTO dto = new DayDTO();
            dto.setId(entity.getId());
            dto.setDate(entity.getDate());
            dto.setCity(entity.getCity());
            return dto;
        } else {
            return null;
        }
    }
    
    private static DayEntity basicDTO2Entity(DayDTO dto) {
        if (dto != null) {
            DayEntity entity = new DayEntity();
            entity.setId(dto.getId());
            entity.setDate(dto.getDate());
            entity.setCity(dto.getCity());
            return entity;
        } else {
            return null;
        }
    }

    public static DayDTO fullEntity2DTO(DayEntity entity) {
        if (entity != null) {
            DayDTO dto = basicEntity2DTO(entity);
            return dto;
        } else {
            return null;
        }
    }

    public static DayEntity fullDTO2Entity(DayDTO dto) {
        if (dto != null) {
            DayEntity entity = basicDTO2Entity(dto);
            return entity;
        } else {
            return null;
        }
    }

    public static List<DayDTO> listEntity2DTO(List<DayEntity> entities) {
        List<DayDTO> dtos = new ArrayList<>();
        if (entities != null) {
            for (DayEntity entity : entities) {
                dtos.add(basicEntity2DTO(entity));
            }
        }
        return dtos;
    }

    public static List<DayEntity> listDTO2Entity(List<DayDTO> dtos) {
        List<DayEntity> entities = new ArrayList<>();
        if (dtos != null) {
            for (DayDTO dto : dtos) {
                entities.add(basicDTO2Entity(dto));
            }
        }
        return entities;
    }
}
