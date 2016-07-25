package co.edu.uniandes.csw.tripulator.persistence;

import co.edu.uniandes.csw.tripulator.entities.DayEntity;
import co.edu.uniandes.csw.tripulator.entities.EventEntity;
import co.edu.uniandes.csw.tripulator.entities.TravellerEntity;
import co.edu.uniandes.csw.tripulator.entities.TripEntity;
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
    private EventPersistence eventPersistence;

    private TravellerEntity traveller;

    private TripEntity trip;

    private DayEntity day;

    private List<EventEntity> data = new ArrayList<>();

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
        em.createQuery("delete from EventEntity").executeUpdate();
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
        day = factory.manufacturePojo(DayEntity.class);
        day.setTrip(trip);
        em.persist(day);
        for (int i = 0; i < 3; i++) {
            EventEntity entity = factory.manufacturePojo(EventEntity.class);
            entity.setDay(day);
            em.persist(entity);
            data.add(entity);
        }
    }

    @Test
    public void createEventTest() {
        EventEntity newEntity = factory.manufacturePojo(EventEntity.class);
        EventEntity result = eventPersistence.create(newEntity);

        Assert.assertNotNull(result);

        EventEntity entity = em.find(EventEntity.class, result.getId());

        Assert.assertEquals(newEntity.getName(), entity.getName());
        Assert.assertEquals(newEntity.getDescription(), entity.getDescription());
        Assert.assertEquals(newEntity.getArrivalDate(), entity.getArrivalDate());
        Assert.assertEquals(newEntity.getDepartureDate(), entity.getDepartureDate());
    }

    @Test
    public void getEventsTest() {
        List<EventEntity> list = eventPersistence.findAll(day.getId());
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
    public void getEventTest() {
        EventEntity entity = data.get(0);
        EventEntity newEntity = eventPersistence.find(day.getId(), entity.getId());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getName(), newEntity.getName());
        Assert.assertEquals(entity.getDescription(), newEntity.getDescription());
        Assert.assertEquals(entity.getArrivalDate(), newEntity.getArrivalDate());
        Assert.assertEquals(entity.getDepartureDate(), newEntity.getDepartureDate());
    }

    @Test
    public void deleteEventTest() {
        EventEntity entity = data.get(0);
        eventPersistence.delete(entity.getId());
        EventEntity deleted = em.find(EventEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    @Test
    public void updateEventTest() {
        EventEntity entity = data.get(0);
        EventEntity newEntity = factory.manufacturePojo(EventEntity.class);
        newEntity.setId(entity.getId());

        eventPersistence.update(newEntity);

        EventEntity resp = em.find(EventEntity.class, entity.getId());

        Assert.assertEquals(newEntity.getName(), resp.getName());
        Assert.assertEquals(newEntity.getDescription(), resp.getDescription());
        Assert.assertEquals(newEntity.getArrivalDate(), resp.getArrivalDate());
        Assert.assertEquals(newEntity.getDepartureDate(), resp.getDepartureDate());
    }

}
