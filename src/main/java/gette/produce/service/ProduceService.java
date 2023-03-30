package gette.produce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gette.exception.ProduceListNotFoundException;
import gette.exception.ProduceNotFoundException;
import gette.exception.ProducePriceNotFoundException;
import gette.produce.dto.ProduceRequestDTO;
import gette.produce.dto.ProduceResponseDTO;
import gette.produce.dto.Token;
import gette.produce.util.ProduceType;
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
public class ProduceService {

    private final RestTemplate restTemplate;

    public ProduceResponseDTO getProducePrice(ProduceRequestDTO request) throws JsonProcessingException {
        //API요청에 필요한 토큰 발급
        Token token = getToken(request.getProduceType());
        //음식 목록 리스트 조회
        String [] produceList = getProduceList(request.getProduceType(), token);
        //유효한 음식인지 확인
        if(!isValidProduce(request.getName(), produceList)){
            throw new ProduceNotFoundException();
        }
        return getProducePrice(request, token);
    }

    public Token getToken(ProduceType type) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity(type.getTokenUrl(), String.class);
        return new Token(getAuthorization(response));
    }

    public String[] getProduceList(ProduceType type, Token token){
        String url = type.getProduceUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token.getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String[]> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String[].class);

        } catch (RestClientException e) {
            throw new ProduceListNotFoundException();
        }
        return response.getBody();
    }

    public boolean isValidProduce(String name, String[] list){
        return Arrays.asList(list).contains(name);
    }

    public ProduceResponseDTO getProducePrice(ProduceRequestDTO request, Token token){
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(request.getProduceType().getProduceUrl())
                .queryParam("name", request.getName());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token.getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<ProduceResponseDTO> response;
        try {
            response = restTemplate.exchange(builder.build(false).toString(), HttpMethod.GET, entity, ProduceResponseDTO.class);
        } catch (RestClientException e) {
            throw new ProducePriceNotFoundException();
        }
        return response.getBody();
    }

    /**
     * 헤더에 토큰 값이 없을 경우 responseBody 에서 토큰값을 가져옴
     */
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