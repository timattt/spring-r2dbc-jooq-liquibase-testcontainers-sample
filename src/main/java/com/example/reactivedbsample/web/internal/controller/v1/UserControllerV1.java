package com.example.reactivedbsample.web.internal.controller.v1;

import com.example.reactivedbsample.bl.operation.api.service.UserService;
import com.example.reactivedbsample.web.api.dto.v1.ExampleUserDtoV1;
import com.example.reactivedbsample.web.internal.mapper.v1.UserMapperV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.example.reactivedbsample.web.internal.EndpointsList.GET_USER_ENDPOINT;
import static com.example.reactivedbsample.web.internal.EndpointsList.CREATE_USER_ENDPOINT;

@RestController
@RequiredArgsConstructor
public class UserControllerV1 {
    static final String EMAIL_QUERY_PARAM = "email";
    static final String PASSWORD_QUERY_PARAM = "password";

    final UserService userService;
    final UserMapperV1 userMapper;

    @GetMapping(GET_USER_ENDPOINT)
    public Mono<ExampleUserDtoV1> findUserByEmail(@RequestParam(EMAIL_QUERY_PARAM) String email) {
        return userService
                .findByEmail(email)
                .map(userMapper::mapToDto);
    }

    @PostMapping(CREATE_USER_ENDPOINT)
    public Mono<ExampleUserDtoV1> createUser(@RequestParam(EMAIL_QUERY_PARAM) String email,
                                             @RequestParam(PASSWORD_QUERY_PARAM) String password) {
        return userService
                .createUser(email, password)
                .map(userMapper::mapToDto);
    }
}
