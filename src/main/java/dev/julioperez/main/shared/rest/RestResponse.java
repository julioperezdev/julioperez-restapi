package dev.julioperez.main.shared.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse<T> implements Serializable {

    private static final long serialVersionUID = 668939847449144032L;

    private Integer statusCode;
    private String status;
    private String message;
    private T body;

    public RestResponse(HttpStatus httpStatus, T body){
        this.statusCode = httpStatus.value();
        this.status = httpStatus.getReasonPhrase();
        this.body = body;
    }

    public RestResponse(HttpStatus httpStatus, String message){
        this.statusCode = httpStatus.value();
        this.status = httpStatus.getReasonPhrase();
        this.message = message;
    }

    public RestResponse(HttpStatus httpStatus, String message, T body){
        this.statusCode = httpStatus.value();
        this.status = httpStatus.getReasonPhrase();
        this.message = message;
        this.body = body;
    }
}
