package com.example.reactivedbsample.persistence.internal.impl.repository;

import io.r2dbc.pool.ConnectionPool;
import org.jooq.DSLContext;
import org.jooq.Publisher;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.connection.ConnectionHolder;
import org.springframework.transaction.reactive.TransactionContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.function.Function;

public abstract class BaseRepository {
    @Autowired
    private ConnectionPool connectionPool;
    @Autowired
    private DSLContext defaultDslContext;

    protected <T> Mono<T> withDslMono(Function<DSLContext, Mono<T>> function) {
        return Mono.deferContextual(ctx -> {
            DSLContext dsl = extractDsl(ctx);
            return function.apply(dsl);
        });
    }

    protected <T> Flux<T> withDslFlux(Function<DSLContext, Flux<T>> function) {
        return Flux.deferContextual(ctx -> {
            DSLContext dsl = extractDsl(ctx);
            return function.apply(dsl);
        });
    }

    protected <T> Mono<T> withDsl(Function<DSLContext, Publisher<T>> function) {
        return Mono.deferContextual(ctx -> {
            DSLContext dsl = extractDsl(ctx);
            return Mono.from(function.apply(dsl));
        });
    }

    private DSLContext extractDsl(ContextView ctx) {
        try {
            TransactionContext context = ctx.getOrDefault(TransactionContext.class, null);
            ConnectionHolder holder = (ConnectionHolder) context.getResources().get(connectionPool);
            return DSL.using(holder.getConnection(), SQLDialect.POSTGRES);
        } catch (Exception ignored) {
            return defaultDslContext;
        }
    }
}
