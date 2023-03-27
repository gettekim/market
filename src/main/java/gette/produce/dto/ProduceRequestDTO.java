package gette.produce.dto;

import gette.produce.util.ProduceType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProduceRequestDTO {

    @NotNull
    private ProduceType produceType;
    @NotBlank(message = "이름을 입력하여 주세요.")
    private String  name;

}
