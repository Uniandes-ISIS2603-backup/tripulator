/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.tripulator.persistence;

import co.edu.uniandes.csw.tripulator.entities.EventEntity;
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
 * @author Jose Daniel Fandiño
 */
@RunWith(Arquillian.class)
public class EventPersistenceTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(EventEntity.class.getPackage())
                .addPackage(EventPersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

    @Inject
    private EventPersistence eventoPersistence;

    @PersistenceContext
    private EntityManager em;

    @Inject
    UserTransaction utx;

    private final PodamFactory factory = new PodamFactoryImpl();

     @Before
    public void configTest() {
        try {
            utx.begin();
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
        em.createQuery("delete from EventoEntity").executeUpdate();
    }

    private List<EventEntity> data = new ArrayList<>();

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            EventEntity entity = factory.manufacturePojo(EventEntity.class);
            em.persist(entity);
            data.add(entity);
        }
    }

    @Test
    public void createEventoTest() {
        EventEntity newEntity = factory.manufacturePojo(EventEntity.class);
        EventEntity result = eventoPersistence.create(newEntity);

        Assert.assertNotNull(result);

        EventEntity entity = em.find(EventEntity.class, result.getId());

        Assert.assertEquals(newEntity.getName(), entity.getName());
        Assert.assertEquals(newEntity.getDescription(), entity.getDescription());
        Assert.assertEquals(newEntity.getCiudad(), entity.getCiudad());
        Assert.assertEquals(newEntity.getImage(), entity.getImage());
        Assert.assertEquals(newEntity.getArrivalDate(), entity.getArrivalDate());
        Assert.assertEquals(newEntity.getDepartureDate(), entity.getDepartureDate());
        Assert.assertEquals(newEntity.getType(), entity.getType());
    }

    @Test
    public void getEventosTest() {
        List<EventEntity> list = eventoPersistence.findAll();
        Assert.assertEquals(data.size(), list.size());
        for (EventEntity ent : list) {
            boolean found = false;
            for (EventEntity entity : data) {
                if (ent.getId().equals(entity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test
    public void getEventoTest() {
        EventEntity entity = data.get(0);
        EventEntity newEntity = eventoPersistence.find(entity.getId());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getName(), newEntity.getName());
        Assert.assertEquals(entity.getDescription(), newEntity.getDescription());
        Assert.assertEquals(entity.getCiudad(), newEntity.getCiudad());
        Assert.assertEquals(entity.getImage(), newEntity.getImage());
        Assert.assertEquals(entity.getArrivalDate(), newEntity.getArrivalDate());
        Assert.assertEquals(entity.getDepartureDate(), newEntity.getDepartureDate());
        Assert.assertEquals(entity.getType(), newEntity.getType());
    }

    @Test
    public void deleteEventoTest() {
        EventEntity entity = data.get(0);
        eventoPersistence.delete(entity.getId());
        EventEntity deleted = em.find(EventEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    @Test
    public void updateEventoTest() {
        EventEntity entity = data.get(0);
        EventEntity newEntity = factory.manufacturePojo(EventEntity.class);
        newEntity.setId(entity.getId());

        eventoPersistence.update(newEntity);

        EventEntity resp = em.find(EventEntity.class, entity.getId());

        Assert.assertEquals(newEntity.getName(), resp.getName());
        Assert.assertEquals(newEntity.getDescription(), resp.getDescription());
        Assert.assertEquals(newEntity.getCiudad(), resp.getCiudad());
        Assert.assertEquals(newEntity.getImage(), resp.getImage());
        Assert.assertEquals(newEntity.getArrivalDate(), resp.getArrivalDate());
        Assert.assertEquals(newEntity.getDepartureDate(), resp.getDepartureDate());
        Assert.assertEquals(newEntity.getType(), resp.getType());
    }

}
