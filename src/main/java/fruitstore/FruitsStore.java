package fruitstore;

import fruitstore.util.FruitUtil;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class FruitsStore {
    private static final Logger LOGGER = Logger.getLogger(FruitsStore.class);

    private List<Fruits> fruits = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Fruits> filterByDate(LocalDate targetDate, boolean spoiled) {
        List<Fruits> listOfFruits = new ArrayList<>();
        fruits.forEach(fruit -> {
            LocalDate dateOfSpoil = fruit.getDateOfHarvest();
            if (spoiled) {
                if (targetDate.isAfter(dateOfSpoil)) {
                    listOfFruits.add(fruit);
                }
            } else {
                if (targetDate.isBefore(dateOfSpoil) || targetDate.isEqual(dateOfSpoil)) {
                    listOfFruits.add(fruit);
                }
            }
        });
        return listOfFruits;
    }

    private List<Fruits> filterByType(FruitsType type, List<Fruits> list) {
        List<Fruits> fruitsList;
        return fruitsList = list.stream().filter(fruit -> fruit.getFruitsType()
                .equals(type))
                .collect(Collectors.toList());
    }

    public void addFruits(String toJsonFile) {
        try (FileInputStream inputStream = new FileInputStream(toJsonFile)) {
            List<Fruits> fruitList;
            fruitList = objectMapper.readValue(inputStream, new TypeReference<List<Fruits>>() {
            });
            fruits.addAll(fruitList);
        } catch (IOException e) {
            LOGGER.error("Exception throw: ", e);
        }
    }

    public void save(String toJsonFile) {
        try (FileOutputStream outputStream = new FileOutputStream(toJsonFile)) {
            objectMapper.writeValue(outputStream, fruits);
        } catch (IOException e) {
            LOGGER.error("Exception throw: ", e);
        }
    }

    public void load(String toJsonFile) {
        fruits.clear();
        try (FileInputStream inputStream = new FileInputStream(toJsonFile)) {
            fruits = objectMapper.readValue(inputStream, new TypeReference<List<Fruits>>() {
            });
        } catch (IOException e) {
            LOGGER.error("Exception throw: ", e);
        }
    }

    public List<Fruits> getSpoiledFruits(LocalDate targetDate) {
        return filterByDate(targetDate, true);
    }

    public List<Fruits> getSpoiledFruits(LocalDate targetDate, FruitsType type) {
        return filterByType(type, getSpoiledFruits(targetDate));
    }

    public List<Fruits> getAvailableFruits(LocalDate targetDate) {
        return filterByDate(targetDate, false);
    }

    public List<Fruits> getAvailableFruits(LocalDate targetDate, FruitsType type) {
        return filterByType(type, getAvailableFruits(targetDate));
    }

    public List<Fruits> getAddedFruits(LocalDate date) {
        List<Fruits> listOfAddedFruits;
        return listOfAddedFruits = fruits.stream()
                .filter(fruits -> fruits.getDateOfDelivery().equals(date))
                .collect(Collectors.toList());
    }

    public List<Fruits> getAddedFruits(LocalDate date, FruitsType type) {
        return filterByType(type, getAddedFruits(date));
    }

    public List<Fruits> getFruitsStorage() {
        return fruits;
    }
}
