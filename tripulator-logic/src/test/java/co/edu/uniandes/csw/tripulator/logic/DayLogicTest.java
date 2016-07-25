package co.edu.uniandes.csw.tripulator.logic;

import co.edu.uniandes.csw.tripulator.ejbs.DayLogic;
import co.edu.uniandes.csw.tripulator.entities.DayEntity;
import co.edu.uniandes.csw.tripulator.entities.EventEntity;
import co.edu.uniandes.csw.tripulator.entities.TripEntity;
import co.edu.uniandes.csw.tripulator.entities.TravellerEntity;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.tripulator.persistence.DayPersistence;
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
import co.edu.uniandes.csw.tripulator.api.IDayLogic;

@RunWith(Arquillian.class)
public class DayLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private IDayLogic dayLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private TravellerEntity traveller;
    private TripEntity trip;
    private List<DayEntity> data = new ArrayList<>();
    private List<EventEntity> eventData = new ArrayList<>();

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(DayEntity.class.getPackage())
                .addPackage(DayLogic.class.getPackage())
                .addPackage(IDayLogic.class.getPackage())
                .addPackage(DayPersistence.class.getPackage())
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
        for (int i = 0; i < 3; i++) {
            DayEntity entity = factory.manufacturePojo(DayEntity.class);
            entity.setTrip(trip);
            em.persist(entity);
            data.add(entity);
        }
        for (int i=0; i<3; i++){
            EventEntity event = factory.manufacturePojo(EventEntity.class);
            if (i==0){
                event.setDay(data.get(0));
            }
            em.persist(event);
            eventData.add(event);
        }
    }
    
    @Test
    public void createDayTest() throws BusinessLogicException {
        DayEntity expected = factory.manufacturePojo(DayEntity.class);
        DayEntity created = dayLogic.createDay(traveller.getId(), trip.getId(), expected);
        
        DayEntity result = em.find(DayEntity.class, created.getId());
        
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getDate(), result.getDate());
        Assert.assertEquals(expected.getCity(), result.getCity());
    }

    @Test
    public void getDaysTest() throws BusinessLogicException {
        List<DayEntity> resultList = dayLogic.getDays(traveller.getId(), trip.getId());
        TypedQuery<DayEntity> q = em.createQuery("select u from "
                    + "DayEntity u where (u.trip.id = :idTrip)",
                    DayEntity.class);
        q.setParameter("idTrip", trip.getId());
        List<DayEntity> expectedList = q.getResultList();
        Assert.assertEquals(expectedList.size(), resultList.size());
        for (DayEntity expected : expectedList) {
            boolean found = false;
            for (DayEntity result : resultList) {
                if (result.getId().equals(expected.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }    
    }
    
    @Test
    public void getDayTest() throws BusinessLogicException {
        DayEntity result = dayLogic.getDay(traveller.getId(), trip.getId(), data.get(0).getId());

        DayEntity expected = em.find(DayEntity.class, data.get(0).getId());

        Assert.assertNotNull(expected);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getDate(), result.getDate());
        Assert.assertEquals(expected.getCity(), result.getCity());
    }
    
    @Test
    public void deleteDayTest() throws BusinessLogicException {
        DayEntity entity = data.get(1);
        dayLogic.deleteDay(traveller.getId(), trip.getId(), entity.getId());
        DayEntity expected = em.find(DayEntity.class, entity.getId());
        Assert.assertNull(expected);
    }

    @Test
    public void updateDayTest() throws BusinessLogicException {
        DayEntity entity = data.get(0);
        DayEntity expected = factory.manufacturePojo(DayEntity.class);

        expected.setId(entity.getId());

        dayLogic.updateDay(traveller.getId(),trip.getId(),expected);

        DayEntity resp = em.find(DayEntity.class, entity.getId());

        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getId(), resp.getId());
        Assert.assertEquals(expected.getDate(), resp.getDate());
        Assert.assertEquals(expected.getCity(), resp.getCity());
    }
    
    @Test
    public void listEventsTest(){
        List<EventEntity> list = dayLogic.getEvents(traveller.getId(), trip.getId(), data.get(0).getId());
        Assert.assertEquals(1, list.size());
    }
    
    @Test
    public void getEventTest(){
        DayEntity entity = data.get(0);
        EventEntity eventEntity = eventData.get(0);
        EventEntity response = dayLogic.getEvent(traveller.getId(), trip.getId(), entity.getId(), eventEntity.getId());

        Assert.assertEquals(eventEntity.getName(), response.getName());
        Assert.assertEquals(eventEntity.getDescription(), response.getDescription());
        Assert.assertEquals(eventEntity.getArrivalDate(), response.getArrivalDate());
        Assert.assertEquals(eventEntity.getDepartureDate(), response.getDepartureDate());
    }
    
    @Test
    public void addEventTest(){
        DayEntity entity = data.get(0);
        EventEntity eventEntity = eventData.get(1);
        EventEntity response = dayLogic.addEvent(traveller.getId(), trip.getId(), entity.getId(), eventEntity);

        Assert.assertNotNull(response);
        Assert.assertEquals(eventEntity.getId(), response.getId());
    }
    
    @Test
    public void replaceEventsTest(){
            try {
            DayEntity entity = data.get(0);
            List<EventEntity> list = eventData.subList(1, 3);
            dayLogic.replaceEvents(traveller.getId(), trip.getId(), entity.getId(), list);

            entity = dayLogic.getDay(traveller.getId(), trip.getId(), entity.getId());
            Assert.assertFalse(entity.getEvents().contains(eventData.get(0)));
            Assert.assertTrue(entity.getEvents().contains(eventData.get(1)));
            Assert.assertTrue(entity.getEvents().contains(eventData.get(2)));
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }
    
    @Test
    public void removeEventTest(){
        dayLogic.removeEvent(traveller.getId(), trip.getId(), data.get(0).getId(), eventData.get(0).getId());
        EventEntity response = dayLogic.getEvent(traveller.getId(), trip.getId(), data.get(0).getId(),eventData.get(0).getId());
        Assert.assertNull(response);
    }
}
