package fruitstore.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FruitUtil {
    private static final String DATE_PATTERN = "dd/MM/yy";

    public static LocalDate dateToString(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return LocalDate.parse(date, dateTimeFormatter);
    }
}
