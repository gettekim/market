package gette.produce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gette.produce.dto.ProduceRequestDTO;
import gette.produce.dto.ProduceResponseDTO;
import gette.produce.dto.Token;
import gette.produce.util.ProduceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProduceServiceTest {

    @Autowired
    private ProduceService produceService;

    @Test
    public void getToken() throws JsonProcessingException {
        //given, when
        Token token = produceService.getToken(ProduceType.FRUIT);
        //then
        assertNotNull(token);
        assertNotNull(token.getAccessToken());
    }

    @Test
    public void getProduceList() throws JsonProcessingException {
        //given
        Token token = produceService.getToken(ProduceType.FRUIT);
        //when
        String[] produceList = produceService.getProduceList(ProduceType.FRUIT, token);
        //then
        assertNotNull(produceList);
        assertTrue(produceList.length > 0);
    }

    @Test
    public void isValidFruit() throws JsonProcessingException {
        //given
        Token token = produceService.getToken(ProduceType.FRUIT);
        //when
        String[] produceList = produceService.getProduceList(ProduceType.FRUIT, token);
        //then
        assertTrue(produceService.isValidProduce("사과", produceList));
        assertFalse(produceService.isValidProduce("수박", produceList));
    }

    @Test
    public void isValidVegetable() throws JsonProcessingException {
        //given
        Token token = produceService.getToken(ProduceType.VEGETABLE);
        //when
        String[] produceList = produceService.getProduceList(ProduceType.VEGETABLE, token);
        //then
        assertTrue(produceService.isValidProduce("토마토", produceList));
        assertFalse(produceService.isValidProduce("당근", produceList));
    }

    @Test
    public void getFruitPrice() throws JsonProcessingException {
        //given
        ProduceRequestDTO request = new ProduceRequestDTO();
        request.setName("사과");
        request.setProduceType(ProduceType.FRUIT);
        //when
        ProduceResponseDTO response = produceService.getProducePrice(request);
        //then
        assertNotNull(response);
        assertEquals("사과", response.getName());
        assertTrue(response.getPrice() > 0);
    }

    @Test
    public void getVegetablePrice() throws JsonProcessingException {
        //given
        ProduceRequestDTO request = new ProduceRequestDTO();
        request.setName("토마토");
        request.setProduceType(ProduceType.VEGETABLE);
        //when
        ProduceResponseDTO response = produceService.getProducePrice(request);
        //then
        assertNotNull(response);
        assertEquals("토마토", response.getName());
        assertTrue(response.getPrice() > 0);
    }

    @Test
    public void getFruitPriceBadRequest() {
        //given
        ProduceRequestDTO request = new ProduceRequestDTO();
        request.setName("포도");
        request.setProduceType(ProduceType.FRUIT);
        //when, then
        assertThrows(IllegalArgumentException.class, () -> produceService.getProducePrice(request));
    }


    @Test
    public void getVegetablePriceBadRequest() {
        //given
        ProduceRequestDTO request = new ProduceRequestDTO();
        request.setName("당근");
        request.setProduceType(ProduceType.VEGETABLE);
        //when, then
        assertThrows(IllegalArgumentException.class, () -> produceService.getProducePrice(request));
    }
}