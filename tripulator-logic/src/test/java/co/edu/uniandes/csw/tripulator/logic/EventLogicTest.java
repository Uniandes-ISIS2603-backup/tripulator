package co.edu.uniandes.csw.tripulator.logic;

import co.edu.uniandes.csw.tripulator.entities.EventEntity;
import co.edu.uniandes.csw.tripulator.entities.TripEntity;
import co.edu.uniandes.csw.tripulator.entities.TravellerEntity;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
import co.edu.uniandes.csw.tripulator.api.IEventLogic;
import co.edu.uniandes.csw.tripulator.ejbs.EventLogic;
import co.edu.uniandes.csw.tripulator.entities.DayEntity;
import co.edu.uniandes.csw.tripulator.persistence.EventPersistence;
import java.util.Date;

@RunWith(Arquillian.class)
public class EventLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private IEventLogic eventLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private TravellerEntity traveller;
    private TripEntity trip;
    private DayEntity day;
    private List<EventEntity> data = new ArrayList<>();

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(EventEntity.class.getPackage())
                .addPackage(EventLogic.class.getPackage())
                .addPackage(IEventLogic.class.getPackage())
                .addPackage(EventPersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

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
        data.clear();
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
            EventEntity event = factory.manufacturePojo(EventEntity.class);
            if (event.getArrivalDate().after(event.getDepartureDate())) {
                Date temp = event.getArrivalDate();
                event.setArrivalDate(event.getDepartureDate());
                event.setDepartureDate(temp);
            }
            event.setDay(day);
            em.persist(event);
            data.add(event);
        }
    }

    @Test
    public void createEventTest() throws BusinessLogicException {
        EventEntity expected = factory.manufacturePojo(EventEntity.class);
        if (expected.getArrivalDate().after(expected.getDepartureDate())) {
            Date temp = expected.getArrivalDate();
            expected.setArrivalDate(expected.getDepartureDate());
            expected.setDepartureDate(temp);
        }
        EventEntity created = eventLogic.createEvent(traveller.getId(), trip.getId(), day.getId(), expected);

        EventEntity result = em.find(EventEntity.class, created.getId());

        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getName(), result.getName());
        Assert.assertEquals(expected.getDescription(), result.getDescription());
        Assert.assertEquals(expected.getArrivalDate(), result.getArrivalDate());
        Assert.assertEquals(expected.getDepartureDate(), result.getDepartureDate());
    }

    @Test
    public void getEventsTest() throws BusinessLogicException {
        List<EventEntity> resultList = eventLogic.getEvents(traveller.getId(), trip.getId(), day.getId());
        TypedQuery<EventEntity> q = em.createQuery("select u from "
                + "EventEntity u where (u.day.id = :idDay)",
                EventEntity.class);
        q.setParameter("idDay", day.getId());
        List<EventEntity> expectedList = q.getResultList();
        Assert.assertEquals(expectedList.size(), resultList.size());
        for (EventEntity expected : expectedList) {
            boolean found = false;
            for (EventEntity result : resultList) {
                if (result.getId().equals(expected.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test
    public void getEventTest() throws BusinessLogicException {
        EventEntity result = eventLogic.getEvent(traveller.getId(), trip.getId(), day.getId(), data.get(0).getId());

        EventEntity expected = em.find(EventEntity.class, data.get(0).getId());

        Assert.assertNotNull(expected);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getName(), result.getName());
        Assert.assertEquals(expected.getDescription(), result.getDescription());
        Assert.assertEquals(expected.getArrivalDate(), result.getArrivalDate());
        Assert.assertEquals(expected.getDepartureDate(), result.getDepartureDate());
    }

    @Test
    public void deleteEventTest() throws BusinessLogicException {
        EventEntity entity = data.get(1);
        eventLogic.deleteEvent(traveller.getId(), trip.getId(), day.getId(), entity.getId());
        EventEntity expected = em.find(EventEntity.class, entity.getId());
        Assert.assertNull(expected);
    }

    @Test
    public void updateEventTest() throws BusinessLogicException {
        EventEntity entity = data.get(0);
        EventEntity expected = factory.manufacturePojo(EventEntity.class);
        if (expected.getArrivalDate().after(expected.getDepartureDate())) {
            Date temp = expected.getArrivalDate();
            expected.setArrivalDate(expected.getDepartureDate());
            expected.setDepartureDate(temp);
        }
        expected.setId(entity.getId());

        eventLogic.updateEvent(traveller.getId(), trip.getId(), day.getId(), expected);

        EventEntity result = em.find(EventEntity.class, entity.getId());

        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getName(), result.getName());
        Assert.assertEquals(expected.getDescription(), result.getDescription());
        Assert.assertEquals(expected.getArrivalDate(), result.getArrivalDate());
        Assert.assertEquals(expected.getDepartureDate(), result.getDepartureDate());
    }
}
