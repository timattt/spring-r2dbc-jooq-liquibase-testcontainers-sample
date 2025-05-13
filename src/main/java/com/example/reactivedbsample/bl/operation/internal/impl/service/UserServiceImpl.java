package com.example.reactivedbsample.bl.operation.internal.impl.service;

import com.example.reactivedbsample.bl.domain.api.exception.NoSuchUserException;
import com.example.reactivedbsample.bl.domain.api.exception.UserWithSuchEmailExistsException;
import com.example.reactivedbsample.bl.domain.api.model.ExampleUser;
import com.example.reactivedbsample.bl.operation.api.service.UserService;
import com.example.reactivedbsample.bl.operation.spi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;

    @Override
    // @Transactional
    public Mono<ExampleUser> createUser(String email, String password) throws UserWithSuchEmailExistsException {
        Assert.notNull(email, "email must not be null");
        Assert.notNull(password, "password must not be null");
        return userRepository
                .loadByEmailAndPessimisticallyLock(email)
                .doOnNext(user -> {
                    throw new UserWithSuchEmailExistsException();
                })
                .switchIfEmpty(userRepository.save(new ExampleUser(0, email, password)));
    }

    @Override
    // @Transactional
    public Mono<ExampleUser> findByEmail(String email) throws NoSuchUserException {
        Assert.notNull(email, "email must not be null");
        return userRepository
                .loadByEmailAndPessimisticallyLock(email)
                .switchIfEmpty(Mono.error(new NoSuchUserException()));
    }
}
