package co.edu.uniandes.csw.tripulator.logic;

import co.edu.uniandes.csw.tripulator.ejbs.TripLogic;
import co.edu.uniandes.csw.tripulator.entities.DayEntity;
import co.edu.uniandes.csw.tripulator.entities.TripEntity;
import co.edu.uniandes.csw.tripulator.entities.TravellerEntity;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.tripulator.persistence.TripPersistence;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
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
import co.edu.uniandes.csw.tripulator.api.ITripLogic;

@RunWith(Arquillian.class)
public class TripLogicTest {
    private static final Logger logger = Logger.getLogger(TripLogicTest.class.getName());

    private final PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private ITripLogic tripLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private final List<TripEntity> data = new ArrayList<>();

    private List<DayEntity> dayData = new ArrayList<>();

    private TravellerEntity traveller;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(TripEntity.class.getPackage())
                .addPackage(TripLogic.class.getPackage())
                .addPackage(ITripLogic.class.getPackage())
                .addPackage(TripPersistence.class.getPackage())
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
        dayData.clear();
        data.clear();
        em.createQuery("delete from DayEntity").executeUpdate();
        em.createQuery("delete from TripEntity").executeUpdate();
        em.createQuery("delete from TravellerEntity").executeUpdate();
    }

    private void insertData() {

        traveller = factory.manufacturePojo(TravellerEntity.class);
        em.persist(traveller);

        for (int i = 0; i < 3; i++) {
            TripEntity entity = factory.manufacturePojo(TripEntity.class);
            entity.setTraveller(traveller);
            em.persist(entity);
            data.add(entity);
        }

        for (int i = 0; i < 3; i++) {
            DayEntity days = factory.manufacturePojo(DayEntity.class);                        
            days.setTrip(data.get(0));
            em.persist(days);
            dayData.add(days);
        }
    }

    @Test
    public void createTripTest() throws BusinessLogicException {
        TripEntity expected = factory.manufacturePojo(TripEntity.class);
        TripEntity created = tripLogic.createTrip(traveller.getId(), expected);

        TripEntity result = em.find(TripEntity.class, created.getId());

        Assert.assertNotNull(result);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getName(), result.getName());
        Assert.assertEquals(expected.getArrivalDate(), result.getArrivalDate());
        Assert.assertEquals(expected.getDepartureDate(), result.getDepartureDate());
        Assert.assertEquals(expected.getCountry(), result.getCountry());
    }

    @Test
    public void getTripsTest() throws BusinessLogicException {
        List<TripEntity> resultList = tripLogic.getTrips(traveller.getId());
        TypedQuery<TripEntity> q = em.createQuery("select u from "
                    + "TripEntity u where (u.traveller.id = :travellerId)",
                    TripEntity.class);
        q.setParameter("travellerId", traveller.getId());
        List<TripEntity> expectedList = q.getResultList();
        Assert.assertEquals(expectedList.size(), resultList.size());
        for (TripEntity expected : expectedList) {
            boolean found = false;
            for (TripEntity result : resultList) {
                if (result.getId().equals(expected.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test
    public void getTripTest() throws BusinessLogicException {
        TripEntity result = tripLogic.getTrip(traveller.getId(), data.get(0).getId());

        TripEntity expected = em.find(TripEntity.class, data.get(0).getId());

        Assert.assertNotNull(expected);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getName(), result.getName());
        Assert.assertEquals(expected.getArrivalDate(), result.getArrivalDate());
        Assert.assertEquals(expected.getDepartureDate(), result.getDepartureDate());
        Assert.assertEquals(expected.getCountry(), result.getCountry());
    }

    @Test
    public void deleteTripTest() throws BusinessLogicException {
        TripEntity entity = data.get(1);
        tripLogic.deleteTrip(traveller.getId(), entity.getId());
        TripEntity expected = em.find(TripEntity.class, entity.getId());
        Assert.assertNull(expected);
    }

    @Test
    public void updateTripTest() throws BusinessLogicException {
        TripEntity entity = data.get(0);
        TripEntity expected = factory.manufacturePojo(TripEntity.class);

        expected.setId(entity.getId());

        tripLogic.updateTrip(traveller.getId(), expected);

        TripEntity resp = em.find(TripEntity.class, entity.getId());

        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getId(), resp.getId());
        Assert.assertEquals(expected.getName(), resp.getName());
        Assert.assertEquals(expected.getArrivalDate(), resp.getArrivalDate());
        Assert.assertEquals(expected.getDepartureDate(), resp.getDepartureDate());
        Assert.assertEquals(expected.getCountry(), resp.getCountry());
    }

    @Test
    public void getDayTest() {
        TripEntity entity = data.get(0);
        DayEntity dayEntity = dayData.get(0);
        DayEntity response = tripLogic.getDay(traveller.getId(), entity.getId(), dayEntity.getId());

        Assert.assertEquals(dayEntity.getId(), response.getId());
        Assert.assertEquals(dayEntity.getName(), response.getName());
        Assert.assertEquals(dayEntity.getDate(), response.getDate());
        Assert.assertEquals(dayEntity.getCity(), response.getCity());
    }

    @Test
    public void listDaysTest() {
        List<DayEntity> list = tripLogic.getDays(traveller.getId(), data.get(0).getId());
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void addDaysTest() throws BusinessLogicException {
        TripEntity entity = data.get(0);
        DayEntity dayEntity = factory.manufacturePojo(DayEntity.class);
        DayEntity response = tripLogic.addDay(traveller.getId(), entity.getId(), dayEntity);

        Assert.assertNotNull(response);
        Assert.assertEquals(dayEntity.getId(), response.getId());
    }
    
    @Test
    public void replaceDaysTest() {
        try {
            TripEntity entity = data.get(0);
            List<DayEntity> list = dayData.subList(1, 3);
            tripLogic.replaceDays(list, traveller.getId(), entity.getId());  
            entity = tripLogic.getTrip(traveller.getId(), entity.getId());
            
            Assert.assertFalse(entity.getDays().contains(dayData.get(0)));
            Assert.assertTrue(entity.getDays().contains(list.get(0)));
            Assert.assertTrue(entity.getDays().contains(list.get(1)));
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test
    public void removeDaysTest() throws BusinessLogicException {
        tripLogic.removeDay(traveller.getId(), data.get(0).getId(), dayData.get(0).getId());
        DayEntity response = tripLogic.getDay(traveller.getId(), data.get(0).getId(), dayData.get(0).getId());
        Assert.assertNull(response);
    }
}
