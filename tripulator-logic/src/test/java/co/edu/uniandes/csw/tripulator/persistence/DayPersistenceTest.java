/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.tripulator.persistence;

import co.edu.uniandes.csw.tripulator.entities.DayEntity;
import co.edu.uniandes.csw.tripulator.entities.TripEntity;
import co.edu.uniandes.csw.tripulator.entities.TravellerEntity;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(Arquillian.class)
public class DayPersistenceTest {

    @Inject
    private DayPersistence dayPersistence;

    @Inject
    UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    private final PodamFactory factory = new PodamFactoryImpl();
    
    private final List<DayEntity> data = new ArrayList<>();
    
    private TravellerEntity traveller;
    
    private TripEntity trip;
    
    @Deployment
    public static JavaArchive createDeployement() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(DayEntity.class.getPackage())
                .addPackage(DayPersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }
    
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
        em.createQuery("delete from DayEntity").executeUpdate();
        em.createQuery("delete from TripEntity").executeUpdate();
        em.createQuery("delete from TravellerEntity").executeUpdate();
    }

    private void insertData() {
        traveller = factory.manufacturePojo(TravellerEntity.class);
        em.persist(traveller);
        trip = factory.manufacturePojo(TripEntity.class);
        trip.setTraveller(traveller);
        em.persist(trip);
        for (int i = 0; i < 3; i++) {
            DayEntity entity = factory.manufacturePojo(DayEntity.class);
            entity.setTrip(trip);
            em.persist(entity);
            data.add(entity);
        }
    }

    @Test
    public void createDayTest() {
        DayEntity newEntity = factory.manufacturePojo(DayEntity.class);
        DayEntity result = dayPersistence.create(newEntity);

        Assert.assertNotNull(result);
        DayEntity entity = em.find(DayEntity.class, newEntity.getId());
        Assert.assertEquals(newEntity.getDate(), entity.getDate());
        Assert.assertEquals(newEntity.getCity(), entity.getCity());
    }
    
    @Test
    public void getDaysTest() {
        List<DayEntity> list = dayPersistence.findAll(trip.getId());
        Assert.assertEquals(data.size(), list.size());
        for (DayEntity d : list) {
            boolean found = false;
            for (DayEntity e : data){
                if (d.getId().equals(e.getId()))
                    found=true;
            }
            Assert.assertTrue(found);
        }
    }
    
    @Test
    public void getDayTest() {
        DayEntity entity = data.get(0);
        DayEntity newEntity = dayPersistence.find(trip.getId(), entity.getId());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(newEntity.getDate(), entity.getDate());
        Assert.assertEquals(newEntity.getCity(), entity.getCity());
    }
    
    @Test
    public void deleteDayTest() {
        DayEntity entity = data.get(0);
        dayPersistence.delete(entity.getId());
        DayEntity deleted = em.find(DayEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }
    
    @Test
    public void updateDayTest() {
        DayEntity entity = data.get(0);
        DayEntity newEntity = factory.manufacturePojo(DayEntity.class);
        newEntity.setId(entity.getId());

        dayPersistence.update(newEntity);

        DayEntity resp = em.find(DayEntity.class, entity.getId());

        Assert.assertEquals(resp.getDate(), newEntity.getDate());
        Assert.assertEquals(resp.getCity(), newEntity.getCity());
    }
}