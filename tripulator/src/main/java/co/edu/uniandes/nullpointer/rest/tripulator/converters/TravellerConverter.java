package co.edu.uniandes.nullpointer.rest.tripulator.converters;

import co.edu.uniandes.csw.tripulator.entities.TravellerEntity;
import co.edu.uniandes.nullpointer.rest.tripulator.dtos.TravellerDTO;
import java.util.ArrayList;
import java.util.List;

public abstract class TravellerConverter {

    private TravellerConverter() {
    }

    public static TravellerDTO refEntity2DTO(TravellerEntity entity) {
        if (entity != null) {
            TravellerDTO dto = new TravellerDTO();
            dto.setId(entity.getId());
            
            return dto;
        } else {
            return null;
        }
    }

    public static TravellerEntity refDTO2Entity(TravellerDTO dto) {
        if (dto != null) {
            TravellerEntity entity = new TravellerEntity();
            entity.setId(dto.getId());

            return entity;
        } else {
            return null;
        }
    }

    private static TravellerDTO basicEntity2DTO(TravellerEntity entity) {
        if (entity != null) {
            TravellerDTO dto = new TravellerDTO();
            dto.setId(entity.getId());
            dto.setPassword(entity.getPassword());
            dto.setUser(entity.getUser());
            return dto;
        } else {
            return null;
        }
    }

    private static TravellerEntity basicDTO2Entity(TravellerDTO dto) {
        if (dto != null) {
            TravellerEntity entity = new TravellerEntity();
            entity.setId(dto.getId());
            entity.setPassword(dto.getPassword());
            entity.setUser(dto.getUser());
            return entity;
        } else {
            return null;
        }
    }

    public static TravellerDTO fullEntity2DTO(TravellerEntity entity) {
        if (entity != null) {
            TravellerDTO dto = basicEntity2DTO(entity);
            return dto;
        } else {
            return null;
        }
    }

    public static TravellerEntity fullDTO2Entity(TravellerDTO dto) {
        if (dto != null) {
            TravellerEntity entity = basicDTO2Entity(dto);
            return entity;
        } else {
            return null;
        }
    }

    public static List<TravellerDTO> listEntity2DTO(List<TravellerEntity> entities) {
        List<TravellerDTO> dtos = new ArrayList<TravellerDTO>();
        if (entities != null) {
            for (TravellerEntity entity : entities) {
                dtos.add(basicEntity2DTO(entity));
            }
        }
        return dtos;
    }

    public static List<TravellerEntity> listDTO2Entity(List<TravellerDTO> dtos) {
        List<TravellerEntity> entities = new ArrayList<TravellerEntity>();
        if (dtos != null) {
            for (TravellerDTO dto : dtos) {
                entities.add(basicDTO2Entity(dto));
            }
        }
        return entities;
    }
}
