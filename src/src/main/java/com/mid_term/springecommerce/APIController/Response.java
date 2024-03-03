package com.mid_term.springecommerce.APIController;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Response<T> {

    private int statusCode;
    private String message;
    private int totalRecord;
    private T data;

    public static <T> Response<T> createSuccessResponseModel(int totalRecord, T data) {
        return new Response<>(200, "Successful", totalRecord, data);
    }

    public static <T> Response<T> createErrorResponseModel(String message, T data) {
        return new Response<>(400, message, 0, data);
    }

    public static <T> Response<T> createResponseModel(int statusCode, String message, T data) {
        return new Response<>(statusCode, message, 0, data);
    }

}

