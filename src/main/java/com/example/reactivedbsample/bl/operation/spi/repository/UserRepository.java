package com.example.reactivedbsample.bl.operation.spi.repository;

import com.example.reactivedbsample.bl.domain.api.model.ExampleUser;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<ExampleUser> loadByEmailAndPessimisticallyLock(String email);
    Mono<ExampleUser> save(ExampleUser user);
}
