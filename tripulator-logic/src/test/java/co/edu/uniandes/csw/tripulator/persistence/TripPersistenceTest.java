/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.tripulator.persistence;

import co.edu.uniandes.csw.tripulator.entities.TripEntity;
import co.edu.uniandes.csw.tripulator.entities.TravellerEntity;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 *
 * @itinerario Antonio de la Vega
 */
@RunWith(Arquillian.class)
public class TripPersistenceTest {
    
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(TripEntity.class.getPackage())
                .addPackage(TripPersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

    @Inject
    private TripPersistence itinerarioPersistence;

    @PersistenceContext
    private EntityManager em;

    @Inject
    UserTransaction utx;

    private final PodamFactory factory = new PodamFactoryImpl();
    
    private final List<TripEntity> data = new ArrayList<>();
    
    private TravellerEntity viajero;
    
    @Before
    public void configTest() {
        try {
            utx.begin();
            em.joinTransaction();
            clearData();
            insertData();
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void clearData() {
        em.createQuery("delete from ItinerarioEntity").executeUpdate();
        em.createQuery("delete from ViajeroEntity").executeUpdate();
    }

    private void insertData() {
        viajero = factory.manufacturePojo(TravellerEntity.class);
        em.persist(viajero);
        for (int i = 0; i < 3; i++) {
            TripEntity entity = factory.manufacturePojo(TripEntity.class);
            entity.setTraveller(viajero);
            em.persist(entity);
            data.add(entity);            
        }
    }

    @Test
    public void createItinerarioTest() {
        TripEntity newEntity = factory.manufacturePojo(TripEntity.class);
        TripEntity result = itinerarioPersistence.create(newEntity);

        Assert.assertNotNull(result);

        TripEntity entity = em.find(TripEntity.class, result.getId());

        Assert.assertEquals(newEntity.getName(), entity.getName());
    }

    @Test
    public void getItinerariosTest() {
        List<TripEntity> list = itinerarioPersistence.findAll(viajero.getId());
        Assert.assertEquals(data.size(), list.size());
        for(TripEntity ent : list) {
            boolean found = false;
            for (TripEntity entity : data) {
                if (ent.getId().equals(entity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test
    public void getItinerarioTest() {
        TripEntity entity = data.get(0);
        TripEntity newEntity = itinerarioPersistence.find(viajero.getId(),entity.getId());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getName(), newEntity.getName());
        Assert.assertEquals(entity.getArrivalDate(), newEntity.getArrivalDate());
        Assert.assertEquals(entity.getDepartureDate(), newEntity.getDepartureDate());        
    }

    @Test
    public void deleteItinerarioTest() {
        TripEntity entity = data.get(0);
        itinerarioPersistence.delete(entity.getId());
        TripEntity deleted = em.find(TripEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    @Test
    public void updateItinerarioTest() {
        TripEntity entity = data.get(0);
        TripEntity newEntity = factory.manufacturePojo(TripEntity.class);

        newEntity.setId(entity.getId());

        itinerarioPersistence.update(newEntity);

        TripEntity resp = em.find(TripEntity.class, entity.getId());

        Assert.assertEquals(newEntity.getName(), resp.getName());
    }
    
}
