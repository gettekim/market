package gette.market.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gette.market.dto.FoodRequestDTO;
import gette.market.dto.FoodResponseDTO;
import gette.market.service.MarketService;
import gette.market.util.FoodType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("foodType", FoodType.getOptions());
        return "home";
    }

    @RequestMapping("/price")
    public ResponseEntity<FoodResponseDTO> getFoodPrice(@Valid @RequestBody FoodRequestDTO request) throws JsonProcessingException {
        FoodResponseDTO foodResponseDTO = marketService.getFoodPrice(request);
        return ResponseEntity.ok(foodResponseDTO);
    }
}
