package com.example.reactivedbsample.integration;

import com.example.reactivedbsample.testcontainers.PostgresTestContainer;
import com.example.reactivedbsample.bl.operation.api.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SystemTests implements PostgresTestContainer {
    static final String TEST_EMAIL = "test@example.com";
    static final String TEST_PASSWORD = "test";

    @Autowired
    UserService userService;

    @Test
    void simpleSystemOperationTest() {
        userService.createUser(TEST_EMAIL, TEST_PASSWORD).block();
        var user = userService.findByEmail(TEST_EMAIL).block();

        Assertions.assertNotNull(user);
        Assertions.assertEquals(TEST_PASSWORD, user.password());
    }
}
