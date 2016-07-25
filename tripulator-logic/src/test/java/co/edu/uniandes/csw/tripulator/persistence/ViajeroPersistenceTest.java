/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.tripulator.persistence;

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

@RunWith(Arquillian.class)
public class ViajeroPersistenceTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(TravellerEntity.class.getPackage())
                .addPackage(TravellerPersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

    @Inject
    private TravellerPersistence viajeroPersistence;

    @PersistenceContext
    private EntityManager em;

    @Inject
    UserTransaction utx;

    private final PodamFactory factory = new PodamFactoryImpl();

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
        em.createQuery("delete from ViajeroEntity").executeUpdate();
    }

    private List<TravellerEntity> data = new ArrayList<>();

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            TravellerEntity entity = factory.manufacturePojo(TravellerEntity.class);
            em.persist(entity);
            data.add(entity);
        }
    }

    @Test
    public void createViajeroTest() {
        TravellerEntity newEntity = factory.manufacturePojo(TravellerEntity.class);
        TravellerEntity result = viajeroPersistence.create(newEntity);

        Assert.assertNotNull(result);

        TravellerEntity entity = em.find(TravellerEntity.class, result.getId());

        Assert.assertEquals(newEntity.getName(), entity.getName());
    }

    @Test
    public void getViajerosTest() {
        List<TravellerEntity> list = viajeroPersistence.findAll();
        Assert.assertEquals(data.size(), list.size());
        for (TravellerEntity ent : list) {
            boolean found = false;
            for (TravellerEntity entity : data) {
                if (ent.getId().equals(entity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test
    public void getViajeroTest() {
        TravellerEntity entity = data.get(0);
        TravellerEntity newEntity = viajeroPersistence.find(entity.getId());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getName(), newEntity.getName());
        Assert.assertEquals(entity.getApellido(), newEntity.getApellido());
        Assert.assertEquals(entity.getPassword(), newEntity.getPassword());
        Assert.assertEquals(entity.getEmail(), newEntity.getEmail());
        Assert.assertEquals(entity.getUser(), newEntity.getUser());
        
        
    }

    @Test
    public void deleteViajeroTest() {
        TravellerEntity entity = data.get(0);
        viajeroPersistence.delete(entity.getId());
        TravellerEntity deleted = em.find(TravellerEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    @Test
    public void updateViajeroTest() {
        TravellerEntity entity = data.get(0);
        TravellerEntity newEntity = factory.manufacturePojo(TravellerEntity.class);

        newEntity.setId(entity.getId());

        viajeroPersistence.update(newEntity);

        TravellerEntity resp = em.find(TravellerEntity.class, entity.getId());

        Assert.assertEquals(newEntity.getName(), resp.getName());
    }
}
