package com.example.reactivedbsample.web.internal;

import com.example.reactivedbsample.bl.domain.api.exception.NoSuchUserException;
import com.example.reactivedbsample.bl.domain.api.exception.UserWithSuchEmailExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class CustomControllerAdvice {
    @ExceptionHandler(NoSuchUserException.class)
    public Mono<ResponseEntity<?>> handleNoSuchUserException(NoSuchUserException ignored) {
        return Mono.just(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(UserWithSuchEmailExistsException.class)
    public Mono<ResponseEntity<?>> handleUserWithSuchEmailExistsException(UserWithSuchEmailExistsException ignored) {
        return Mono.just(ResponseEntity.unprocessableEntity().build());
    }
}
