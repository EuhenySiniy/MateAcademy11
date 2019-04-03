package fruitstore;

import fruitstore.util.FruitUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fruits {
    private FruitsType fruitsType;
    private int shelfLife;
    private int price;
    private String dateOfDelivery;

    @JsonIgnore
    public LocalDate getDateOfHarvest() {
        LocalDate dateOfDelivery = FruitUtil.dateToString(getDateOfDelivery());
        return dateOfDelivery.plusDays(getShelfLife());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Fruits fruits = (Fruits) obj;
        return getFruitsType() == fruits.getFruitsType()
                && (shelfLife == fruits.shelfLife)
                && (price == fruits.price)
                && (dateOfDelivery.equals(fruits.getDateOfDelivery()));
    }
}
