package dev.julioperez.main.shared.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;

@ControllerAdvice(annotations = RestController.class)
public class RestExceptionInterceptor {

    @ExceptionHandler
    public ResponseEntity<Object> processSupportedExceptions(Throwable throwable){
        Optional<ResponseEntity> supportedException = Arrays.stream(SupportedExceptions.values())
                .filter(particularSupportedException -> hasSameClass(particularSupportedException, throwable))
                .map(this::createResponseEntityFromException)
                .findFirst();

        return supportedException.isPresent()
                ? supportedException.get() :
                new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Boolean hasSameClass(SupportedExceptions exception, Throwable throwable){
        return throwable.getClass().equals(exception.getExceptionClass());
    }

    private ResponseEntity createResponseEntityFromException(SupportedExceptions exception){
        ExceptionResponse object = new ExceptionResponse(
                exception.getStatus().getReasonPhrase(),
                exception.getStatus().value(),
                exception.getExceptionClass().getSimpleName()
        );
        return new ResponseEntity(object,exception.getStatus());
    }
}
