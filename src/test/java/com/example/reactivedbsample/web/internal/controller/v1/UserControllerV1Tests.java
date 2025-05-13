package com.example.reactivedbsample.web.internal.controller.v1;

import com.example.reactivedbsample.bl.domain.api.exception.NoSuchUserException;
import com.example.reactivedbsample.bl.domain.api.exception.UserWithSuchEmailExistsException;
import com.example.reactivedbsample.bl.domain.api.model.ExampleUser;
import com.example.reactivedbsample.bl.operation.api.service.UserService;
import com.example.reactivedbsample.web.api.dto.v1.ExampleUserDtoV1;
import com.example.reactivedbsample.web.internal.mapper.v1.UserMapperV1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.example.reactivedbsample.web.internal.EndpointsList.CREATE_USER_ENDPOINT;
import static com.example.reactivedbsample.web.internal.EndpointsList.GET_USER_ENDPOINT;
import static com.example.reactivedbsample.web.internal.controller.v1.UserControllerV1.EMAIL_QUERY_PARAM;
import static com.example.reactivedbsample.web.internal.controller.v1.UserControllerV1.PASSWORD_QUERY_PARAM;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient
@WebFluxTest(controllers = UserControllerV1.class)
public class UserControllerV1Tests {
    static final String TEST_EMAIL = "test@example.com";
    static final String TEST_PASSWORD = "testpassword";
    static final long TEST_ID = 1L;
    static final ExampleUser USER = new ExampleUser(TEST_ID, TEST_EMAIL, TEST_PASSWORD);
    static final ExampleUserDtoV1 USER_DTO = new ExampleUserDtoV1(TEST_ID, TEST_EMAIL, TEST_PASSWORD);

    @Autowired
    WebTestClient webClient;
    @MockitoBean
    UserService userService;
    @MockitoBean
    UserMapperV1 userMapperV1;

    @BeforeEach
    public void setupMapper() {
        when(userMapperV1.mapToDto(eq(USER))).thenReturn(USER_DTO);
    }

    @Nested
    class findUserByEmailTests {
        @Test
        public void okTest() {
            when(userService.findByEmail(eq(TEST_EMAIL))).thenReturn(Mono.just(USER));

            var result = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(GET_USER_ENDPOINT)
                            .queryParam(EMAIL_QUERY_PARAM, TEST_EMAIL)
                            .build())
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(ExampleUserDtoV1.class)
                    .returnResult()
                    .getResponseBody();

            Assertions.assertEquals(USER_DTO, result);

            verify(userService).findByEmail(eq(TEST_EMAIL));
        }

        @Test
        public void noSuchUserTest() {
            when(userService.findByEmail(eq(TEST_EMAIL))).thenReturn(Mono.error(new NoSuchUserException()));

            webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(GET_USER_ENDPOINT)
                            .queryParam(EMAIL_QUERY_PARAM, TEST_EMAIL)
                            .build())
                    .exchange()
                    .expectStatus()
                    .isNotFound();

            verify(userService).findByEmail(eq(TEST_EMAIL));
        }
    }

    @Nested
    class createUserTests {
        @Test
        public void okTest() {
            when(userService.createUser(eq(TEST_EMAIL), eq(TEST_PASSWORD))).thenReturn(Mono.just(USER));

            var result = webClient
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path(CREATE_USER_ENDPOINT)
                            .queryParam(EMAIL_QUERY_PARAM, TEST_EMAIL)
                            .queryParam(PASSWORD_QUERY_PARAM, TEST_PASSWORD)
                            .build())
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(ExampleUserDtoV1.class)
                    .returnResult()
                    .getResponseBody();

            Assertions.assertEquals(USER_DTO, result);

            verify(userService).createUser(eq(TEST_EMAIL), eq(TEST_PASSWORD));
        }

        @Test
        public void userExistsTest() {
            when(userService.createUser(eq(TEST_EMAIL), eq(TEST_PASSWORD))).thenReturn(Mono.error(new UserWithSuchEmailExistsException()));

            webClient
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path(CREATE_USER_ENDPOINT)
                            .queryParam(EMAIL_QUERY_PARAM, TEST_EMAIL)
                            .queryParam(PASSWORD_QUERY_PARAM, TEST_PASSWORD)
                            .build())
                    .exchange()
                    .expectStatus()
                    .is4xxClientError();

            verify(userService).createUser(eq(TEST_EMAIL), eq(TEST_PASSWORD));
        }
    }
}
