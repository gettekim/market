package gette.market.dto;

import gette.market.util.FoodType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FoodRequestDTO {

    @NotNull
    private FoodType foodType;
    @NotBlank(message = "이름을 입력하여 주세요.")
    private String  name;

}
