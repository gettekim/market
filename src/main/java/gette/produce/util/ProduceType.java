package gette.produce.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ProduceType {
    FRUIT("과일","http://fruit.api.postype.net/token","http://fruit.api.postype.net/product"),
    VEGETABLE("야채","http://vegetable.api.postype.net/token","http://vegetable.api.postype.net/item");

    private final String type;
    private final String tokenUrl;
    private final String produceUrl;
    ProduceType(String type, String tokenUrl, String produceUrl) {
        this.type = type;
        this.tokenUrl = tokenUrl;
        this.produceUrl = produceUrl;
    }

    public String getType() {
        return type;
    }
    public String getTokenUrl() {return tokenUrl;}
    public String getProduceUrl() {return produceUrl;}

    //select box에 출력하기위해 선언
    public static List<SelectOption> getOptions() {
        return Arrays.stream(values())
                .map(e -> new SelectOption(e.name(), e.getType()))
                .collect(Collectors.toList());
    }
}
