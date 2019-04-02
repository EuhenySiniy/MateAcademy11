package fruitstore;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class FruitsStoreTest {
    private static final String FIRST_FILE = "firstfile.json";
    private static final String DATABASE = "database.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Logger LOGGER = Logger.getLogger(FruitsStoreTest.class);

    private FruitsStore fruitsStore;
    private List<Fruits> fruits;

    @Before
    public void init() {
        fruitsStore = new FruitsStore();
        fruits = new ArrayList<>();
        Fruits cherry = new Fruits(FruitsType.CHERRY, 12, 20, "10/04/19");
        Fruits apple = new Fruits(FruitsType.APPLE, 15, 10, "12/04/19");
        Fruits mango = new Fruits(FruitsType.MANGO, 10, 80, "05/04/19");
        fruits.add(cherry);
        fruits.add(apple);
        fruits.add(mango);

        try (FileOutputStream outputStream = new FileOutputStream(FIRST_FILE)) {
            OBJECT_MAPPER.writeValue(outputStream, fruits);
        } catch (IOException e) {
            LOGGER.error("Exception throw ", e);
        }
    }

    @Test
    public void addingFruits() {
        fruitsStore.addFruits(FIRST_FILE);
        assertTrue(fruitsStore.getFruitsStorage().containsAll(fruits));
    }

    @Test
    public void savingFruits() {
        fruitsStore.addFruits(FIRST_FILE);
        fruitsStore.save(DATABASE);
        List<Fruits> expected = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(DATABASE)) {
            expected = OBJECT_MAPPER.readValue(inputStream, new TypeReference<List<Fruits>>() {
            });
        } catch (IOException e) {
            LOGGER.error("Exception thrown: ", e);
        }
        assertTrue(fruits.containsAll(expected));
    }

    @Test
    public void loadingFruits() {
        fruitsStore.addFruits(FIRST_FILE);
        try (FileOutputStream outputStream = new FileOutputStream(DATABASE)) {
            OBJECT_MAPPER.writeValue(outputStream, fruitsStore.getFruitsStorage());
        } catch (IOException e) {
            LOGGER.error("Exception thrown: ", e);
        }
        fruitsStore.load(DATABASE);
        assertTrue(fruits.containsAll(fruitsStore.getFruitsStorage()));
    }

    @Test
    public void returningListOfSpoiledFruitsOnTargetDate() {
        fruitsStore.addFruits(FIRST_FILE);
        LocalDate localDate = LocalDate.of(2019, 04, 10);
        List<Fruits> spoiled = fruitsStore.getSpoiledFruits(localDate);
        assertTrue(spoiled.contains(fruits.get(0)));
    }

    @Test
    public void returningListOfSpoiledFruitsOnTargetDateWithType() {
        fruitsStore.addFruits(FIRST_FILE);
        LocalDate localDate = LocalDate.of(2019, 04, 10);
        List<Fruits> spoiled = fruitsStore.getSpoiledFruits(localDate, FruitsType.CHERRY);
        assertTrue(spoiled.contains(fruits.get(0)));
    }

    @Test
    public void returningListOfAvailableFruitsOnTargetDate() {
        fruitsStore.addFruits(FIRST_FILE);
        LocalDate date = LocalDate.of(2019, 4, 10);
        List<Fruits> available = fruitsStore.getAvailableFruits(date);
        int expectedSize = 2;
        assertEquals(available.size(), expectedSize);
        assertTrue(available.contains(fruits.get(1)));
        assertTrue(available.contains(fruits.get(2)));
    }

    @Test
    public void returningListOfAvailableFruitsOnTargetDateWithType() {
        fruitsStore.addFruits(FIRST_FILE);
        LocalDate date = LocalDate.of(2019, 4, 10);
        List<Fruits> available = fruitsStore.getAvailableFruits(date, FruitsType.CHERRY);
        int expectedSize = 1;
        assertEquals(available.size(), expectedSize);
        assertTrue(available.contains(fruits.get(2)));
    }

    @Test
    public void returningListOfAddedFruitsOnTargetDate() {
        fruitsStore.addFruits(FIRST_FILE);
        LocalDate date = LocalDate.of(2019, 4, 12);
        List<Fruits> added = fruitsStore.getAddedFruits(date);
        assertTrue(fruits.containsAll(added));
    }

    @Test
    public void returningListOfAddedFruitsOnTargetDateWithType() {
        fruitsStore.addFruits(FIRST_FILE);
        LocalDate date = LocalDate.of(2019, 4, 12);
        List<Fruits> added = fruitsStore.getAddedFruits(date, FruitsType.MANGO);
        assertEquals(added.get(0), fruits.get(1));
    }
}