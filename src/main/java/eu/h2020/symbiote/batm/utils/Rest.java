package eu.h2020.symbiote.batm.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.h2020.symbiote.batm.dto.BaseModel;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Rest <T extends BaseModel> {

    public T get(String url, Map<String, String> params, HttpHeaders headers, Class<T> clazz ){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (params != null ) {
            for (String key : params.keySet()) {
                builder.queryParam(key, params.get(key));
            }
        }

        url = builder.build().encode().toUri().toString();

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, params );
        JSONObject jo = null;

        try { jo = new JSONObject(response.getBody().toString());
        } catch (JSONException e) {e.printStackTrace();}

        return parseJson(jo,clazz);
    }


    public T post(String url, Map<String, String> params, HttpHeaders headers, Class<T> clazz, Object request ){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (params != null ) {
            for (String key : params.keySet()) {
                builder.queryParam(key, params.get(key));
            }
        }

        url = builder.build().encode().toUri().toString();

        T returns = restTemplate.postForObject(url, request, clazz);

        return returns;
    }



    public Boolean postReturningBooelanOrNull(String url, Map<String, String> params, HttpHeaders headers, Object request ){
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (params != null ) {
            for (String key : params.keySet()) {
                builder.queryParam(key, params.get(key));
            }
        }

        url = builder.build().encode().toUri().toString();

        Boolean returns = restTemplate.postForObject(url, request, Boolean.class);

        return returns;
    }






    private T parseJson(JSONObject jo, Class<T> clazz){

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T d = null;

        try {
            d = mapper.readValue(jo.toString(),clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return d;
    }


}
