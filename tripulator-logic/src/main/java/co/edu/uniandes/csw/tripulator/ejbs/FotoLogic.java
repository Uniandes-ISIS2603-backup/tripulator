/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.tripulator.ejbs;

import co.edu.uniandes.csw.tripulator.api.IFotoLogic;
import co.edu.uniandes.csw.tripulator.entities.FotoEntity;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.tripulator.persistence.FotoPersistence;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author josef
 */
@Stateless
public class FotoLogic implements IFotoLogic{
    
    private static final Logger logger = Logger.getLogger(ViajeroLogic.class.getName());

    @Inject
    private FotoPersistence persistence;
    

    @Override
    public List<FotoEntity> getFotos() {
        logger.info("Inicia proceso de consultar todas las fotos");
        List<FotoEntity> fotos = persistence.findAll();
        logger.info("Termina proceso de consultar todas las fotos");
        return fotos;
    }

    @Override
    public FotoEntity getFoto(Long id) throws BusinessLogicException {
        logger.log(Level.INFO, "Inicia proceso de consultar foto con id={0}", id);
        FotoEntity foto = persistence.find(id);
        if (foto == null) {
            logger.log(Level.SEVERE, "La foto con el id {0} no existe", id);
            throw new BusinessLogicException("La foto solicitada no existe");
        }
        logger.log(Level.INFO, "Termina proceso de consultar foto con id={0}", id);
        return foto;
    }

    @Override
    public FotoEntity createFoto(FotoEntity entity) {
        logger.info("Inicia proceso de creación de foto");
        persistence.create(entity);
        logger.info("Termina proceso de creación de foto");
        return entity;
    }


    @Override
    public void deleteFoto(Long id) {
        logger.log(Level.INFO, "Inicia proceso de borrar foto con id={0}", id);
        persistence.delete(id);
        logger.log(Level.INFO, "Termina proceso de borrar foto con id={0}", id);
    }
    
}