package com.example.reactivedbsample.bl.operation.api.service;

import com.example.reactivedbsample.bl.domain.api.exception.NoSuchUserException;
import com.example.reactivedbsample.bl.domain.api.exception.UserWithSuchEmailExistsException;
import com.example.reactivedbsample.bl.domain.api.model.ExampleUser;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<ExampleUser> createUser(String email, String password) throws UserWithSuchEmailExistsException;
    Mono<ExampleUser> findByEmail(String email) throws NoSuchUserException;
}
