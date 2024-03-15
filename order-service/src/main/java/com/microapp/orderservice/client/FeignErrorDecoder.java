package com.microapp.orderservice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microapp.orderservice.exception.RestException;
import com.microapp.orderservice.exception.RestExceptionError;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public Exception decode(String s, Response response) {

        RestExceptionError errorResponse = mapper.readValue(response.body().asInputStream(), RestExceptionError.class);

        return new RestException(HttpStatus.BAD_REQUEST, Arrays.asList(errorResponse),"");
    };
}
