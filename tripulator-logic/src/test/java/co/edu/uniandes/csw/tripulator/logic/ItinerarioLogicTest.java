/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.tripulator.logic;

import co.edu.uniandes.csw.tripulator.ejbs.TripLogic;
import co.edu.uniandes.csw.tripulator.entities.DayEntity;
import co.edu.uniandes.csw.tripulator.entities.FotoEntity;
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

/**
 *
 * @author Antonio de la Vega
 */
@RunWith(Arquillian.class)
public class ItinerarioLogicTest {
    private static final Logger logger = Logger.getLogger(ItinerarioLogicTest.class.getName());

    private final PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private ITripLogic itinerarioLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private final List<TripEntity> data = new ArrayList<>();

    private List<FotoEntity> fotoData = new ArrayList<>();

    private List<DayEntity> diaData = new ArrayList<>();

    private TravellerEntity viajero;

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
        diaData.clear();
        fotoData.clear();
        data.clear();
        em.createQuery("delete from FotoEntity").executeUpdate();
        em.createQuery("delete from DiaEntity").executeUpdate();
        em.createQuery("delete from ItinerarioEntity").executeUpdate();
        em.createQuery("delete from ViajeroEntity").executeUpdate();
    }

    private void insertData() {

        viajero = factory.manufacturePojo(TravellerEntity.class);
        em.persist(viajero);

        for (int i = 0; i < 3; i++) {
            TripEntity entity = factory.manufacturePojo(TripEntity.class);
            entity.setTraveller(viajero);
            em.persist(entity);
            data.add(entity);
        }

        for (int i = 0; i < 3; i++) {
            FotoEntity fotos = factory.manufacturePojo(FotoEntity.class);

            DayEntity dias = factory.manufacturePojo(DayEntity.class);
            
            fotos.setItinerario(data.get(0));
            
            dias.setTrip(data.get(0));

            em.persist(dias);
            diaData.add(dias);
            em.persist(fotos);
            fotoData.add(fotos);
        }
    }

    @Test
    public void createItinerarioTest() throws BusinessLogicException {
        TripEntity expected = factory.manufacturePojo(TripEntity.class);
        TripEntity created = itinerarioLogic.createTrip(viajero.getId(), expected);

        TripEntity result = em.find(TripEntity.class, created.getId());

        Assert.assertNotNull(result);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getName(), result.getName());
        Assert.assertEquals(expected.getArrivalDate(), result.getArrivalDate());
        Assert.assertEquals(expected.getDepartureDate(), result.getDepartureDate());
    }

    @Test
    public void getItinerariosTest() throws BusinessLogicException {
        List<TripEntity> resultList = itinerarioLogic.getTrips(viajero.getId());
        TypedQuery<TripEntity> q = em.createQuery("select u from "
                    + "ItinerarioEntity u where (u.viajero.id = :idViajero)",
                    TripEntity.class);
        q.setParameter("idViajero", viajero.getId());
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
    public void getItinerarioTest() throws BusinessLogicException {
        TripEntity result = itinerarioLogic.getTrip(viajero.getId(), data.get(0).getId());

        TripEntity expected = em.find(TripEntity.class, data.get(0).getId());

        Assert.assertNotNull(expected);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getName(), result.getName());
        Assert.assertEquals(expected.getArrivalDate(), result.getArrivalDate());
        Assert.assertEquals(expected.getDepartureDate(), result.getDepartureDate());
    }

    @Test
    public void deleteItinerarioTest() throws BusinessLogicException {
        TripEntity entity = data.get(1);
        itinerarioLogic.deleteTrip(viajero.getId(), entity.getId());
        TripEntity expected = em.find(TripEntity.class, entity.getId());
        Assert.assertNull(expected);
    }

    @Test
    public void updateItinerarioTest() throws BusinessLogicException {
        TripEntity entity = data.get(0);
        TripEntity expected = factory.manufacturePojo(TripEntity.class);

        expected.setId(entity.getId());

        itinerarioLogic.updateTrip(viajero.getId(), expected);

        TripEntity resp = em.find(TripEntity.class, entity.getId());

        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getId(), resp.getId());
        Assert.assertEquals(expected.getName(), resp.getName());
        Assert.assertEquals(expected.getArrivalDate(), resp.getArrivalDate());
        Assert.assertEquals(expected.getDepartureDate(), resp.getDepartureDate());
    }

    @Test
    public void getPhotoTest() {
        TripEntity entity = data.get(0);
        FotoEntity fotoEntity = fotoData.get(0);
        FotoEntity response = itinerarioLogic.getPhoto(viajero.getId(), entity.getId(), fotoEntity.getId());

        Assert.assertEquals(fotoEntity.getId(), response.getId());
        Assert.assertEquals(fotoEntity.getName(), response.getName());
        Assert.assertEquals(fotoEntity.getSrc(), response.getSrc());
    }

    @Test
    public void getDayTest() {
        TripEntity entity = data.get(0);
        DayEntity diaEntity = diaData.get(0);
        DayEntity response = itinerarioLogic.getDay(viajero.getId(), entity.getId(), diaEntity.getId());

        Assert.assertEquals(diaEntity.getId(), response.getId());
        Assert.assertEquals(diaEntity.getName(), response.getName());
        Assert.assertEquals(diaEntity.getDate(), response.getDate());
        Assert.assertEquals(diaEntity.getCity(), response.getCity());
    }

    @Test
    public void listPhotosTest() {
        List<FotoEntity> list = itinerarioLogic.getPhotos(viajero.getId(), data.get(0).getId());
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void listDaysTest() {
        List<DayEntity> list = itinerarioLogic.getDays(viajero.getId(), data.get(0).getId());
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void addPhotosTest() throws BusinessLogicException {
        TripEntity entity = data.get(0);
        FotoEntity fotoEntity = factory.manufacturePojo(FotoEntity.class);
        FotoEntity response = itinerarioLogic.addPhoto(viajero.getId(), entity.getId(), fotoEntity);

        Assert.assertNotNull(response);
        Assert.assertEquals(fotoEntity.getId(), response.getId());
    }

    @Test
    public void addDaysTest() throws BusinessLogicException {
        TripEntity entity = data.get(0);
        DayEntity diaEntity = factory.manufacturePojo(DayEntity.class);
        DayEntity response = itinerarioLogic.addDay(viajero.getId(), entity.getId(), diaEntity);

        Assert.assertNotNull(response);
        Assert.assertEquals(diaEntity.getId(), response.getId());
    }

    @Test
    public void replacePhotosTest() {
        try {
            TripEntity entity = data.get(0);
            List<FotoEntity> list = fotoData.subList(1, 3);
            itinerarioLogic.replacePhotos(list, viajero.getId(), entity.getId());

            entity = itinerarioLogic.getTrip(viajero.getId(), entity.getId());
            Assert.assertFalse(entity.getFotos().contains(fotoData.get(0)));
            Assert.assertTrue(entity.getFotos().contains(fotoData.get(1)));
            Assert.assertTrue(entity.getFotos().contains(fotoData.get(2)));
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test
    public void replaceDaysTest() {
        try {
            TripEntity entity = data.get(0);
            List<DayEntity> list = diaData.subList(1, 3);
            for(DayEntity dia : list){
                logger.info("parte 1: " + dia.getDate() + " " + dia.getName() + " " + dia.getCity() + dia.getPais() + dia.getId());
            }
            itinerarioLogic.replaceDays(list, viajero.getId(), entity.getId());
            
            entity = itinerarioLogic.getTrip(viajero.getId(), entity.getId());
            for(DayEntity dia : entity.getDays()){
                logger.info("parte 2: " + dia.getDate() + " " + dia.getName() + " " + dia.getCity() + dia.getPais() + dia.getId());
            }
            
            Assert.assertFalse(entity.getDays().contains(diaData.get(0)));
            Assert.assertTrue(entity.getDays().contains(list.get(0)));
            Assert.assertTrue(entity.getDays().contains(list.get(1)));
        } catch (BusinessLogicException ex) {
            Assert.fail(ex.getLocalizedMessage());
        }
    }

    @Test
    public void removePhotosTest() throws BusinessLogicException {
        itinerarioLogic.removePhoto(viajero.getId(), data.get(0).getId(), fotoData.get(0).getId());
        FotoEntity response = itinerarioLogic.getPhoto(viajero.getId(), data.get(0).getId(), fotoData.get(0).getId());
        Assert.assertNull(response);
    }
    
    @Test
    public void removeDaysTest() throws BusinessLogicException {
        itinerarioLogic.removeDay(viajero.getId(), data.get(0).getId(), diaData.get(0).getId());
        DayEntity response = itinerarioLogic.getDay(viajero.getId(), data.get(0).getId(), diaData.get(0).getId());
        Assert.assertNull(response);
    }
}
