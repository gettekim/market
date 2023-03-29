package gette.produce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gette.produce.dto.ProduceRequestDTO;
import gette.produce.dto.ProduceResponseDTO;
import gette.produce.service.ProduceService;
import gette.produce.util.ProduceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProduceController {

    private final ProduceService produceService;

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("produceType", ProduceType.getOptions());
        return "produce";
    }

    @RequestMapping("/price")
    public ResponseEntity<ProduceResponseDTO> getFoodPrice(@Valid ProduceRequestDTO request) throws JsonProcessingException {
        ProduceResponseDTO produceResponseDTO = produceService.getProducePrice(request);
        return ResponseEntity.ok(produceResponseDTO);
    }
}
