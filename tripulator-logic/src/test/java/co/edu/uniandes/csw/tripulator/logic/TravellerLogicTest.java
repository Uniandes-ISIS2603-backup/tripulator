package co.edu.uniandes.csw.tripulator.logic;

import co.edu.uniandes.csw.tripulator.ejbs.TravellerLogic;
import co.edu.uniandes.csw.tripulator.entities.TravellerEntity;
import co.edu.uniandes.csw.tripulator.entities.TripEntity;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.tripulator.persistence.TravellerPersistence;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
import co.edu.uniandes.csw.tripulator.api.ITravellerLogic;

@RunWith(Arquillian.class)
public class TravellerLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private ITravellerLogic travellerLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<TravellerEntity> data = new ArrayList<>();

    private List<TripEntity> tripsData = new ArrayList<>();

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(TravellerEntity.class.getPackage())
                .addPackage(TravellerLogic.class.getPackage())
                .addPackage(ITravellerLogic.class.getPackage())
                .addPackage(TravellerPersistence.class.getPackage())
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
        tripsData.clear();
        em.createQuery("delete from TripEntity").executeUpdate();
        em.createQuery("delete from TravellerEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            TravellerEntity entity = factory.manufacturePojo(TravellerEntity.class);
            em.persist(entity);
            data.add(entity);

            for (int j = 0; j < 5; j++) {
                TripEntity itinerarioentity = factory.manufacturePojo(TripEntity.class);
                itinerarioentity.setTraveller(entity);
                em.persist(itinerarioentity);
                tripsData.add(itinerarioentity);

            }

        }

    }

    @Test
    public void createTravellerTest() {
        TravellerEntity expected = factory.manufacturePojo(TravellerEntity.class);
        TravellerEntity created = travellerLogic.createTraveller(expected);

        TravellerEntity result = em.find(TravellerEntity.class, created.getId());

        Assert.assertNotNull(result);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getName(), result.getName());
        Assert.assertEquals(expected.getPassword(), result.getPassword());
        Assert.assertEquals(expected.getUser(), result.getUser());
    }

    @Test
    public void getTravellersTest() {
        List<TravellerEntity> resultList = travellerLogic.getTravellers();
        List<TravellerEntity> expectedList = em.createQuery("SELECT u from TravellerEntity u").getResultList();
        Assert.assertEquals(expectedList.size(), resultList.size());
        for (TravellerEntity expected : expectedList) {
            boolean found = false;
            for (TravellerEntity result : resultList) {
                if (result.getId().equals(expected.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test
    public void getTravellerTest() throws BusinessLogicException {
        TravellerEntity expected = em.find(TravellerEntity.class, data.get(0).getId());
        TravellerEntity result = travellerLogic.getTraveller(data.get(0).getId());

        Assert.assertNotNull(expected);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getName(), result.getName());
        Assert.assertEquals(expected.getPassword(), result.getPassword());
        Assert.assertEquals(expected.getUser(), result.getUser());
    }

    @Test
    public void deleteTravellerTest() {
        TravellerEntity entity = data.get(0);
        travellerLogic.deleteTraveller(entity.getId());
        TravellerEntity expected = em.find(TravellerEntity.class, entity.getId());
        Assert.assertNull(expected);
    }

    @Test
    public void updateTravellerTest() {
        TravellerEntity entity = data.get(0);
        TravellerEntity expected = factory.manufacturePojo(TravellerEntity.class);

        expected.setId(entity.getId());

        travellerLogic.updateTraveller(expected);

        TravellerEntity resp = em.find(TravellerEntity.class, entity.getId());

        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getId(), resp.getId());
        Assert.assertEquals(expected.getName(), resp.getName());
        Assert.assertEquals(expected.getPassword(), resp.getPassword());
        Assert.assertEquals(expected.getUser(), resp.getUser());
    }

    @Test
    public void listTripsTest() {
        List<TripEntity> list = travellerLogic.getTrips(data.get(0).getId());
        TravellerEntity expected = em.find(TravellerEntity.class, data.get(0).getId());

        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getTrips().size(), list.size());
    }

    @Test
    public void getTripTest() {
        TravellerEntity entity = data.get(0);
        TripEntity tripEntity = tripsData.get(0);
        TripEntity response = travellerLogic.getTrip(entity.getId(), tripEntity.getId());

        Assert.assertNotNull(response);
        Assert.assertEquals(tripEntity.getId(), response.getId());
        Assert.assertEquals(tripEntity.getName(), response.getName());
        Assert.assertEquals(tripEntity.getArrivalDate(), response.getArrivalDate());
        Assert.assertEquals(tripEntity.getDepartureDate(), response.getDepartureDate());

    }

    @Test
    public void addTripsTest() {
        try {
            TravellerEntity entity = data.get(0);
            TripEntity tripEntity = tripsData.get(0);
            TripEntity response = travellerLogic.addTrip(tripEntity, entity.getId());

            TripEntity expected = getTravellerTrip(entity.getId(), tripEntity.getId());

            Assert.assertNotNull(expected);
            Assert.assertNotNull(response);
            Assert.assertEquals(expected.getId(), response.getId());
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test
    public void removeTripsTest() throws BusinessLogicException {
        TravellerEntity entity = data.get(0);
        TripEntity tripEntity = tripsData.get(0);
        travellerLogic.removeTrip(tripEntity.getId(), entity.getId());
        TripEntity expected = em.find(TripEntity.class, tripEntity.getId());
        Assert.assertNull(expected);
    }

    @Test
    public void replaceTripsTest() {
        try {
            TravellerEntity entity = data.get(0);
            List<TripEntity> list = tripsData.subList(1, 3);
            List<TripEntity> replaced = travellerLogic.replaceTrips(list, entity.getId());
            TravellerEntity expected = travellerLogic.getTraveller(entity.getId());
            Assert.assertNotNull(expected);
            Assert.assertFalse(modifiedContains(expected.getTrips(), tripsData.get(0)));
            Assert.assertTrue(modifiedContains(expected.getTrips(), replaced.get(0)));
            Assert.assertTrue(modifiedContains(expected.getTrips(), replaced.get(1)));
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    private boolean modifiedContains(List<TripEntity> dbTrips, TripEntity trip) {
        for (TripEntity t : dbTrips) {
            if (t.getArrivalDate().equals(trip.getArrivalDate())
                    && t.getCountry().equals(trip.getCountry())
                    && t.getName().equals(trip.getName())
                    && t.getDepartureDate().equals(trip.getDepartureDate())) {
                return true;
            }
        }
        return false;
    }

    private TripEntity getTravellerTrip(Long idTraveller, Long idTrip) {
        Query q = em.createQuery("Select DISTINCT b from TravellerEntity a join a.trips b where a.id=:idTraveller and b.id = :idTrip");
        q.setParameter("idTrip", idTrip);
        q.setParameter("idTraveller", idTraveller);
        return (TripEntity) q.getSingleResult();
    }
}
