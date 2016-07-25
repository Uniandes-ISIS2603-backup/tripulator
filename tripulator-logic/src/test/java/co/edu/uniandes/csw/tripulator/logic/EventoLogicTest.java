package co.edu.uniandes.csw.tripulator.logic;

import co.edu.uniandes.csw.tripulator.ejbs.EventLogic;
import co.edu.uniandes.csw.tripulator.entities.ComentarioEntity;
import co.edu.uniandes.csw.tripulator.entities.DayEntity;
import co.edu.uniandes.csw.tripulator.entities.EventEntity;
import co.edu.uniandes.csw.tripulator.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.tripulator.persistence.EventPersistence;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import co.edu.uniandes.csw.tripulator.api.IEventLogic;

@RunWith(Arquillian.class)
public class EventoLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private IEventLogic eventoLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<EventEntity> data = new ArrayList<EventEntity>();

    private List<DayEntity> diasData = new ArrayList<>();

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
            System.out.println("ERROR VA A HACER ROLLBACK");
                utx.rollback();
            System.out.println("HIZO ROLLBACK");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void clearData() {
        em.createQuery("delete from ComentarioEntity").executeUpdate();
        em.createQuery("delete from DiaEntity").executeUpdate();
        em.createQuery("delete from EventoEntity").executeUpdate();
    }

    private void insertData() throws Exception{
        for (int i = 0; i < 3; i++) {
            DayEntity dia = factory.manufacturePojo(DayEntity.class);
            System.out.println(dia.getCity());
            em.persist(dia);
            diasData.add(dia);
        }
        String infoData="";
        for (int i = 0; i < 3; i++) {
            EventEntity entity = factory.manufacturePojo(EventEntity.class);
            if(entity.getArrivalDate().after(entity.getDepartureDate())){
                Date temp = entity.getArrivalDate();
                entity.setArrivalDate(entity.getDepartureDate());
             setDepartureDateetFechaFin(temp);
            }
            for (ComentarioEntity item : entity.getComentarios()) {
                item.setEvento(entity);
            }

            entity.getDias().add(diasData.get(0));
            infoData+=" Entity: "+entity.getId()+" , "+entity.getName();
            //em.persist(entity);
            eventoLogic.createEvento(entity);
            data.add(entity);
        }
        System.out.println(infoData);
    }

    @Test
    public void getEventoTest() {
        try {
            EventEntity entity = data.get(0);
            EventEntity resultEntity = eventoLogic.getEvento(entity.getId());
            Assert.assertNotNull(resultEntity);
            Assert.assertEquals(entity.getId(), resultEntity.getId());
            Assert.assertEquals(entity.getName(), resultEntity.getName());
            Assert.assertEquals(entity.getDescription(), resultEntity.getDescription());
            Assert.assertEquals(entity.getCiudad(), resultEntity.getCiudad());
            Assert.assertEquals(entity.getImage(), resultEntity.getImage());
            Assert.assertEquals(entity.getArrivalDate(), resultEntity.getArrivalDate());
            Assert.assertEquals(entity.getDepartureDate(), resultEntity.getDepartureDate());
            Assert.assertEquals(entity.getType(), resultEntity.getType());
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test
    public void getEventosTest() {
        List<EventEntity> list = eventoLogic.getEvents();
        String r ="";
        for (EventEntity entity : list) {
            boolean found = false;
            r+=("Enity: "+entity.getId()+" , "+entity.getName()+" \n");
            for (EventEntity storedEntity : data) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            //Assert.assertTrue(found);
        }
        Assert.assertEquals(r,data.size(), list.size());
    }

    @Test
    public void getEventosCiudadFecha() {
        try {
            System.out.println("VA A EMPEZAR GETEVENTOSCIUDADFECHA");
            List<EventEntity> eventos = eventoLogic.getEvents();
            System.out.println("OBTIENE LISTA DE TODOS LOS EVENTOS");
            for (EventEntity entity : eventos) {
                List<EventEntity> list = eventoLogic.getEventosCiudadFecha(entity.getCiudad(), entity.getArrivalDate());
            System.out.println("OBTIENE LISTA DE TODOS LOS EVENTOS DE "+entity.getCiudad()+" CON FECHA "+entity.getArrivalDate());
                boolean esta = false;
                Date endOfDay = eventoLogic.getEndOfDay(entity.getArrivalDate());
                for (EventEntity actual : list) {

                    if (actual.getArrivalDate().before(entity.getArrivalDate()) || actual.getArrivalDate().after(endOfDay)) {
                        Assert.fail("La lista de eventos con fecha " + entity.getArrivalDate() + " no es correcta,"
                                + " pues contiene un evento con fecha " + actual.getArrivalDate());
                    }
                    Assert.assertEquals("La lista obtenida contiene un evento de ciudad diferente a la buscada.",
                            entity.getCiudad(), actual.getCiudad());
                    if (Objects.equals(entity.getId(), actual.getId())) {
                        esta = true;
                    }
                }
                Assert.assertTrue("La lista de eventos segun ciudad y fecha no contiene un evento que cumple las condiciones", esta);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void createEventoTest() {
        try {
            EventEntity entity = factory.manufacturePojo(EventEntity.class);
            if(entity.getArrivalDate().after(entity.getDepartureDate())){
                Date temp = entity.getArrivalDate();
                entity.setArrivalDate(entity.getDepartureDasetDepartureDate            entity.setFechaFin(temp);
            }
            EventEntity result = eventoLogic.createEvento(entity);

            EventEntity resp = em.find(EventEntity.class, result.getId());

            Assert.assertNotNull(resp);
            Assert.assertEquals(entity.getId(), resp.getId());
            Assert.assertEquals(entity.getName(), resp.getName());
            Assert.assertEquals(entity.getDescription(), resp.getDescription());
            Assert.assertEquals(entity.getCiudad(), resp.getCiudad());
            Assert.assertEquals(entity.getImage(), resp.getImage());
            Assert.assertEquals(entity.getArrivalDate(), resp.getArrivalDate());
            Assert.assertEquals(entity.getDepartureDate(), resp.getDepartureDate());
            Assert.assertEquals(entity.getType(), resp.getType());
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test
    public void deleteEventoTest() {
        EventEntity entity = data.get(1);
        eventoLogic.deleteEvento(entity.getId());
        EventEntity deleted = em.find(EventEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    @Test
    public void updateEventoTest() {
        try {
            EventEntity entity = data.get(1);
            EventEntity pojoEntity = factory.manufacturePojo(EventEntity.class);
            pojoEntity.setId(entity.getId());
            if(pojoEntity.getArrivalDate().after(pojoEntity.getDepartureDate())){
                Date temp = pojoEntity.getArrivalDate();
                pojoEntity.setArrivalDate(pojoEntitsetDepartureDateureDate());
                pojoEntity.setFechaFin(temp);
            }
            eventoLogic.updateEvento(pojoEntity);
            EventEntity resp = eventoLogic.getEvento(entity.getId());
            //EventoEntity resp = em.find(EventEntity.class, entity.getId());

            Assert.assertNotNull("La respuesta no deberia ser nulla", resp);
            Assert.assertEquals(pojoEntity.getId(), resp.getId());
            Assert.assertEquals(pojoEntity.getName(), resp.getName());
            Assert.assertEquals(pojoEntity.getDescription(), resp.getDescription());
            Assert.assertEquals(pojoEntity.getCiudad(), resp.getCiudad());
            Assert.assertEquals(pojoEntity.getImage(), resp.getImage());
            Assert.assertEquals(pojoEntity.getArrivalDate(), resp.getArrivalDate());
            Assert.assertEquals(pojoEntity.getDepartureDate(), resp.getDepartureDate());
            Assert.assertEquals(pojoEntity.getType(), resp.getType());
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test
    public void listDatesTest() {
        try {
            List<DayEntity> list = eventoLogic.getDias(data.get(0).getId());
            EventEntity expected = em.find(EventEntity.class, data.get(0).getId());

            Assert.assertNotNull(expected);
            Assert.assertEquals(expected.getDias().size(), list.size());
        } catch (Exception e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    private DayEntity getEventoDia(Long eventoId, Long diaId) {
        Query q = em.createQuery("Select DISTINCT d from EventoEntity e join e.dias d where e.id = :eventoId and d.id=:diaId");
        q.setParameter("eventoId", eventoId);
        q.setParameter("diaId", diaId);

        return (DayEntity) q.getSingleResult();
    }
}
