package gette.market.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gette.market.dto.FoodRequestDTO;
import gette.market.dto.FoodResponseDTO;
import gette.market.dto.Token;
import gette.market.util.FoodType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketService {

    private final RestTemplate restTemplate;

    public FoodResponseDTO getFoodPrice(FoodRequestDTO request) throws JsonProcessingException {
        //API요청에 필요한 토큰 발급
        Token token = getToken(request.getFoodType());
        //음식 목록 리스트 조회
        String [] foodList = getFoodList(request.getFoodType(), token);
        //유효한 음식인지 확인
        if(!isValidFood(request.getName(), foodList)){
            throw new IllegalArgumentException("유효하지 않은 음식입니다.");
        }
        return getFoodPrice(request, token);
    }

    public Token getToken(FoodType type) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity(type.getTokenUrl(), String.class);
        return new Token(getAuthorization(response));
    }

    public String[] getFoodList(FoodType type, Token token){
        String url = type.getFoodUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token.getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String[]> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String[].class);

        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }

        if(response.getBody() == null){
            throw  new NullPointerException();
        }
        return response.getBody();
    }

    public boolean isValidFood(String name, String[] list){
        return Arrays.asList(list).contains(name);
    }

    public FoodResponseDTO getFoodPrice(FoodRequestDTO request,Token token){
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(request.getFoodType().getFoodUrl())
                .queryParam("name", request.getName());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token.getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<FoodResponseDTO> response;
        try {
            response = restTemplate.exchange(builder.build(false).toString(), HttpMethod.GET, entity, FoodResponseDTO.class);
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
        if(response.getBody() == null){
            throw  new NullPointerException();
        }
        return response.getBody();
    }

    //토큰값 출력
    public String getAuthorization(ResponseEntity<String> response) throws JsonProcessingException {
        String authorizationValue = getAuthorizationFromCookie(response);
        if (authorizationValue == null) {
            Token token = getTokenFromResponseBody(response);
            return token.getAccessToken();
        }
        return authorizationValue;
    }

    private String getAuthorizationFromCookie(ResponseEntity<String> response) {
        HttpHeaders headers = response.getHeaders();
        String setCookie = headers.getFirst("Set-Cookie");
        if (setCookie != null) {
            String[] cookie = setCookie.split(";");
            for (String item : cookie) {
                if (item.trim().startsWith("Authorization=")) {
                    return item.substring("Authorization=".length());
                }
            }
        }
        return null;
    }

    private Token getTokenFromResponseBody(ResponseEntity<String> response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.getBody(), Token.class);
    }

}