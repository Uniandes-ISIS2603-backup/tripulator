package co.edu.uniandes.csw.tripulator.logic;

import co.edu.uniandes.csw.tripulator.ejbs.TravellerLogic;
import co.edu.uniandes.csw.tripulator.entities.TravellerEntity;
import co.edu.uniandes.csw.tripulator.entities.TripEntity;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.tripulator.persistence.TravellerPersistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
public class ViajeroLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private ITravellerLogic viajeroLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<TravellerEntity> data = new ArrayList<TravellerEntity>();

    private List<TripEntity> itinerariosData = new ArrayList<TripEntity>();

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
        
        em.createQuery("delete from ItinerarioEntity").executeUpdate();
        em.createQuery("delete from ViajeroEntity").executeUpdate();
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
                itinerariosData.add(itinerarioentity);

            }

        }

    }

    @Test
    public void createViajeroTest() {
        TravellerEntity expected = factory.manufacturePojo(TravellerEntity.class);
        TravellerEntity created = viajeroLogic.createTraveller(expected);

        TravellerEntity result = em.find(TravellerEntity.class, created.getId());

        Assert.assertNotNull(result);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getName(), result.getName());
        Assert.assertEquals(expected.getApellido(), result.getApellido());
        Assert.assertEquals(expected.getPassword(), result.getPassword());
        Assert.assertEquals(expected.getEmail(), result.getEmail());
        Assert.assertEquals(expected.getUser(), result.getUser());
    }

    @Test
    public void getViajerosTest() {
        List<TravellerEntity> resultList = viajeroLogic.getTravellers();
        List<TravellerEntity> expectedList = em.createQuery("SELECT u from ViajeroEntity u").getResultList();
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
    public void getViajeroTest() throws BusinessLogicException {
        System.out.println(data.get(0).getId());
        TravellerEntity expected = em.find(TravellerEntity.class, data.get(0).getId());
        TravellerEntity result = viajeroLogic.getTraveller(data.get(0).getId());

        Assert.assertNotNull(expected);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getName(), result.getName());
        Assert.assertEquals(expected.getApellido(), result.getApellido());
        Assert.assertEquals(expected.getPassword(), result.getPassword());
        Assert.assertEquals(expected.getEmail(), result.getEmail());
        Assert.assertEquals(expected.getUser(), result.getUser());
    }

    @Test
    public void deleteViajeroTest() {
        TravellerEntity entity = data.get(0);
        viajeroLogic.deleteTraveller(entity.getId());
        TravellerEntity expected = em.find(TravellerEntity.class, entity.getId());
        Assert.assertNull(expected);
    }

    @Test
    public void updateViajeroTest() {
        TravellerEntity entity = data.get(0);
        TravellerEntity expected = factory.manufacturePojo(TravellerEntity.class);

        expected.setId(entity.getId());

        viajeroLogic.updateTraveller(expected);

        TravellerEntity resp = em.find(TravellerEntity.class, entity.getId());

        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getId(), resp.getId());
        Assert.assertEquals(expected.getName(), resp.getName());
        Assert.assertEquals(expected.getApellido(), resp.getApellido());
        Assert.assertEquals(expected.getPassword(), resp.getPassword());
        Assert.assertEquals(expected.getEmail(), resp.getEmail());
        Assert.assertEquals(expected.getUser(), resp.getUser());
    }

    @Test
    public void listItinerariosTest() {
        List<TripEntity> list = viajeroLogic.getTrips(data.get(0).getId());
        TravellerEntity expected = em.find(TravellerEntity.class, data.get(0).getId());

        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getTrips().size(), list.size());
    }

    @Test
    public void getItinerarioTest() {
        TravellerEntity entity = data.get(0);
        TripEntity itinerarioEntity = itinerariosData.get(0);
        TripEntity response = viajeroLogic.getTrip(entity.getId(), itinerarioEntity.getId());

        
        Assert.assertNotNull(response);
        Assert.assertEquals(itinerarioEntity.getId(), response.getId());
        Assert.assertEquals(itinerarioEntity.getName(), response.getName());
        Assert.assertEquals(itinerarioEntity.getArrivalDate(), response.getArrivalDate());
        Assert.assertEquals(itinerarioEntity.getDepartureDate(), response.getDepartureDate());

    }

    @Test
    public void addItinerariosTest() {
        try {
            TravellerEntity entity = data.get(0);
            TripEntity itinerarioEntity = itinerariosData.get(0);
            TripEntity response = viajeroLogic.addTrip(itinerarioEntity, entity.getId());

            TripEntity expected = getViajeroItinerario(entity.getId(), itinerarioEntity.getId());

            Assert.assertNotNull(expected);
            Assert.assertNotNull(response);
            Assert.assertEquals(expected.getId(), response.getId());
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test
    public void removeItinerariosTest() throws BusinessLogicException {
        TravellerEntity entity = data.get(0);
        TripEntity itinerarioEntity = itinerariosData.get(0);
        viajeroLogic.removeTrip(itinerarioEntity.getId(), entity.getId());
        TripEntity expected = em.find(TripEntity.class, itinerarioEntity.getId());
        Assert.assertNull(expected);
    }

    @Test
    public void replaceItinerariosTest() {
        try {
            TravellerEntity entity = data.get(0);
            List<TripEntity> list = itinerariosData.subList(1, 3);
            viajeroLogic.replaceTrips(list, entity.getId());

            TravellerEntity expected = viajeroLogic.getTraveller(entity.getId());
            Assert.assertNotNull(expected);
            Assert.assertFalse(expected.getTrips().contains(itinerariosData.get(0)));
            Assert.assertTrue(expected.getTrips().contains(itinerariosData.get(1)));
            Assert.assertTrue(expected.getTrips().contains(itinerariosData.get(2)));
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    private TripEntity getViajeroItinerario(Long viajeroId, Long itinerarioId) {
        Query q = em.createQuery("Select DISTINCT b from ViajeroEntity a join a.itinerarios b where a.id=:viajeroId and b.id = :itinerarioId");
        q.setParameter("itinerarioId", itinerarioId);
        q.setParameter("viajeroId", viajeroId);

        return (TripEntity) q.getSingleResult();
    }
}
