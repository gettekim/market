package gette.market.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum FoodType {
    FRUIT("과일","http://fruit.api.postype.net/token","http://fruit.api.postype.net/product"),
    VEGETABLE("야채","http://vegetable.api.postype.net/token","http://vegetable.api.postype.net/item");

    private final String type;
    private final String tokenUrl;
    private final String foodUrl;
    FoodType(String type, String tokenUrl, String foodUrl) {
        this.type = type;
        this.tokenUrl = tokenUrl;
        this.foodUrl = foodUrl;
    }

    public String getType() {
        return type;
    }
    public String getTokenUrl() {return tokenUrl;}
    public String getFoodUrl() {return foodUrl;}

    //select box에 출력하기위해 선언
    public static List<SelectOption> getOptions() {
        return Arrays.stream(values())
                .map(e -> new SelectOption(e.name(), e.getType()))
                .collect(Collectors.toList());
    }
}
