package com.example.reactivedbsample.persistence.internal.impl.repository;

import com.example.reactivedbsample.bl.domain.api.model.ExampleUser;
import com.example.reactivedbsample.bl.operation.spi.repository.UserRepository;
import com.example.reactivedbsample.generated.Tables;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserRepositoryImpl extends BaseRepository implements UserRepository {
    @Override
    public Mono<ExampleUser> loadByEmailAndPessimisticallyLock(String email) {
        return dsl(dsl -> dsl
                        .selectFrom(Tables.EXAMPLE_USER)
                        .where(Tables.EXAMPLE_USER.EMAIL.eq(email))
                        .forUpdate())
            .map(r -> r.into(ExampleUser.class));
    }

    @Override
    public Mono<ExampleUser> save(ExampleUser user) {
        if (user.id() == 0) {
            return dsl(dsl -> dsl
                            .insertInto(Tables.EXAMPLE_USER)
                            .set(Tables.EXAMPLE_USER.EMAIL, user.email())
                            .set(Tables.EXAMPLE_USER.PASSWORD, user.password())
                            .returningResult(Tables.EXAMPLE_USER.USER_ID))
                    .map(r -> new ExampleUser(r.value1(), user.email(), user.password()));
        }
        return dsl(dsl -> dsl
                .update(Tables.EXAMPLE_USER)
                .set(Tables.EXAMPLE_USER.EMAIL, user.email())
                .set(Tables.EXAMPLE_USER.PASSWORD, user.password())
                .where(Tables.EXAMPLE_USER.USER_ID.eq(user.id()))
                .returningResult(Tables.EXAMPLE_USER.USER_ID)
        ).map(r -> user);
    }
}
