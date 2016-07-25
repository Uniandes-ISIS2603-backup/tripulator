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
public class DiaLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private IDayLogic diaLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private TravellerEntity viajero;
    private TripEntity itinerario;
    private List<DayEntity> data = new ArrayList<>();
    private List<EventEntity> eventoData = new ArrayList<>();

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
        em.createQuery("delete from EventoEntity").executeUpdate();
        em.createQuery("delete from DiaEntity").executeUpdate();
        em.createQuery("delete from ItinerarioEntity").executeUpdate();
        em.createQuery("delete from ViajeroEntity").executeUpdate();
    }

    private void insertData() {
        
        viajero = factory.manufacturePojo(TravellerEntity.class);
        em.persist(viajero);
        itinerario = factory.manufacturePojo(TripEntity.class);
        itinerario.setTraveller(viajero);
        em.persist(itinerario);
        for (int i = 0; i < 3; i++) {
            DayEntity entity = factory.manufacturePojo(DayEntity.class);
            entity.setTrip(itinerario);
            em.persist(entity);
            data.add(entity);
        }
        for (int i=0; i<3; i++){
            EventEntity evento = factory.manufacturePojo(EventEntity.class);
            if (i==0){
                evento.setDias(data);
            }
            em.persist(evento);
            eventoData.add(evento);
        }
    }
    
    @Test
    public void createDiaTest() throws BusinessLogicException {
        DayEntity expected = factory.manufacturePojo(DayEntity.class);
        DayEntity created = diaLogic.createDay(viajero.getId(), itinerario.getId(), expected);
        
        DayEntity result = em.find(DayEntity.class, created.getId());
        
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getDate(), result.getDate());
        Assert.assertEquals(expected.getCity(), result.getCity());
        Assert.assertEquals(expected.getPais(), result.getPais());
    }

    @Test
    public void getDiasTest() throws BusinessLogicException {
        List<DayEntity> resultList = diaLogic.getDays(viajero.getId(), itinerario.getId());
        TypedQuery<DayEntity> q = em.createQuery("select u from "
                    + "DiaEntity u where (u.itinerario.id = :idItinerario)",
                    DayEntity.class);
        q.setParameter("idItinerario", itinerario.getId());
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
    public void getDiaTest() throws BusinessLogicException {
        DayEntity result = diaLogic.getDay(viajero.getId(), itinerario.getId(), data.get(0).getId());

        DayEntity expected = em.find(DayEntity.class, data.get(0).getId());

        Assert.assertNotNull(expected);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getDate(), result.getDate());
        Assert.assertEquals(expected.getCity(), result.getCity());
        Assert.assertEquals(expected.getPais(), result.getPais());
    }
    
    @Test
    public void deleteDiaTest() throws BusinessLogicException {
        DayEntity entity = data.get(1);
        diaLogic.deleteDay(viajero.getId(), itinerario.getId(), entity.getId());
        DayEntity expected = em.find(DayEntity.class, entity.getId());
        Assert.assertNull(expected);
    }

    @Test
    public void updateDiaTest() throws BusinessLogicException {
        DayEntity entity = data.get(0);
        DayEntity expected = factory.manufacturePojo(DayEntity.class);

        expected.setId(entity.getId());

        diaLogic.updateDay(viajero.getId(),itinerario.getId(),expected);

        DayEntity resp = em.find(DayEntity.class, entity.getId());

        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getId(), resp.getId());
        Assert.assertEquals(expected.getDate(), resp.getDate());
        Assert.assertEquals(expected.getCity(), resp.getCity());
        Assert.assertEquals(expected.getPais(), resp.getPais());
    }
    
    @Test
    public void listEventosTest(){
        List<EventEntity> list = diaLogic.getEvents(viajero.getId(), itinerario.getId(), data.get(0).getId());
        Assert.assertEquals(1, list.size());
    }
    
    @Test
    public void getEventoTest(){
        DayEntity entity = data.get(0);
        EventEntity eventoEntity = eventoData.get(0);
        EventEntity response = diaLogic.getEvent(viajero.getId(), itinerario.getId(), entity.getId(), eventoEntity.getId());

        Assert.assertEquals(eventoEntity.getName(), response.getName());
        Assert.assertEquals(eventoEntity.getDescription(), response.getDescription());
        Assert.assertEquals(eventoEntity.getCiudad(), response.getCiudad());
        Assert.assertEquals(eventoEntity.getImage(), response.getImage());
        Assert.assertEquals(eventoEntity.getArrivalDate(), response.getArrivalDate());
        Assert.assertEquals(eventoEntity.getDepartureDate(), response.getDepartureDate());
        Assert.assertEquals(eventoEntity.getType(), response.getType());
    }
    
    @Test
    public void addEventoTest(){
        DayEntity entity = data.get(0);
        EventEntity eventoEntity = eventoData.get(1);
        EventEntity response = diaLogic.addEvent(viajero.getId(), itinerario.getId(), entity.getId(), eventoEntity.getId());

        Assert.assertNotNull(response);
        Assert.assertEquals(eventoEntity.getId(), response.getId());
    }
    
    @Test
    public void replaceEventosTest(){
            try {
            DayEntity entity = data.get(0);
            List<EventEntity> list = eventoData.subList(1, 3);
            diaLogic.replaceEvents(viajero.getId(), itinerario.getId(), entity.getId(), list);

            entity = diaLogic.getDay(viajero.getId(), itinerario.getId(), entity.getId());
            Assert.assertFalse(entity.getEvents().contains(eventoData.get(0)));
            Assert.assertTrue(entity.getEvents().contains(eventoData.get(1)));
            Assert.assertTrue(entity.getEvents().contains(eventoData.get(2)));
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }
    
    @Test
    public void removeEventoTest(){
        diaLogic.removeEvent(viajero.getId(), itinerario.getId(), data.get(0).getId(), eventoData.get(0).getId());
        EventEntity response = diaLogic.getEvent(viajero.getId(), itinerario.getId(), data.get(0).getId(),eventoData.get(0).getId());
        Assert.assertNull(response);
    }
}
