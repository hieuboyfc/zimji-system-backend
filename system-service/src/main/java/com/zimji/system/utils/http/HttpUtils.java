package com.zimji.system.utils.http;

import com.zimji.system.payload.response.BaseResponse;
import com.zimji.system.utils.string.StringUtil;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

public class HttpUtils {

    public static String getUrl(Map<String, Object> mapper, String temp) {
        return StringUtil.replace(mapper, temp);
    }

    public static HttpHeaders of(JsonObject headers) {
        return of(headers.getMap());
    }

    public static HttpEntity<?> createRequestEntity(HttpMethod httpMethod, HttpHeaders headers, JsonObject payload) {
        MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        headers.set(HttpHeaders.ACCEPT_ENCODING, "GZIP");

        if (httpMethod == HttpMethod.GET) {
            return new HttpEntity<>(headers);
        } else {
            payload = ObjectUtils.isEmpty(payload) ? new JsonObject() : payload;
            return new HttpEntity<>(payload.encode(), headers);
        }
    }

    public static HttpEntity<?> createGetRequestEntity(HttpHeaders headers) {
        MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        headers.set(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate");
        return new HttpEntity<>(headers);
    }

    public static <T> BaseResponse<T> internalGetRequest(String uri, JsonObject headers, Class<T> clazz) throws Exception {
        return internalRequest(uri, HttpMethod.GET, createGetRequestEntity(of(headers)), clazz);
    }

    public static <T> BaseResponse<T> upload(String uri, HttpMethod method, HttpEntity<?> entity, Class<T> clazz) throws Exception {
        RestTemplate restTemplate;
        try {
            restTemplate = new RestTemplateBuilder().build();
        } catch (Exception e) {
            restTemplate = new RestTemplate();
        }
        return internalRequest(restTemplate, uri, method, entity, clazz);
    }

    public byte[] download(String url, HttpHeaders headers) {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate;
        try {
            restTemplate = new RestTemplateBuilder().build();
        } catch (Exception e) {
            restTemplate = new RestTemplate();
        }
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
        return response.getBody();
    }

    public static <T> BaseResponse<T> internalRequest(String uri, HttpMethod method, HttpEntity<?> entity, Class<T> clazz) throws Exception {
        RestTemplate restTemplate;
        try {
            restTemplate = new RestTemplateBuilder().build();
        } catch (Exception e) {
            restTemplate = new RestTemplate();
        }
        return internalRequest(restTemplate, uri, method, entity, clazz);
    }

    private static HttpHeaders of(Map<String, Object> headers) {
        HttpHeaders result = new HttpHeaders();
        headers.forEach((key, value) -> {
            result.add(key, value.toString());
        });
        return result;
    }

    private static <T> BaseResponse<T> internalRequest(RestTemplate restTemplate,
                                                       String uri,
                                                       HttpMethod method,
                                                       HttpEntity<?> entity,
                                                       Class<T> clazz) throws URISyntaxException {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(BaseResponse.class, clazz);
        ParameterizedTypeReference<BaseResponse<T>> parameterizedTypeReference = ParameterizedTypeReference.forType(resolvableType.getType());
        ResponseEntity<BaseResponse<T>> response = restTemplate.exchange(new URI(uri), method, entity, parameterizedTypeReference);
        return response.getBody();
    }

}