package com.webflux.project.services;

import com.webflux.project.models.Client;
import com.webflux.project.repositories.ClientRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Data
@Slf4j
public class ClientService {

    private  final ClientRepository clientRepository;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final WebClient webClient;


//    public Mono<Integer> saveBatch_ORIGINAL() {
//        var start = Instant.now().toEpochMilli();
//        var insertCount = new AtomicInteger();
//            return r2dbcEntityTemplate.getDatabaseClient()
//                    .inConnectionMany(connection -> getClientFlux()
//                                .map(o -> {o.setName("LOL"); return o;})
//                                .distinct().buffer(10_000).publishOn(Schedulers.boundedElastic())
//                                .flatMap(records -> {
//                                    final var batch = connection.createBatch();
//                                    records.forEach(record->batch.add(Client.buildInsertQuery(record)));
//                                     return batch.execute();
//                                })
//                                .flatMap(r -> Objects.isNull(r) ? Flux.just(0): r.getRowsUpdated())
//                                .doOnNext(n-> {
//                                    if(insertCount.addAndGet(n) % 100_000 == 0){
//                                        log.info("Items insted -> {}", insertCount.get());
//                                    }
//                                })
//                                .reduce(0, Integer::sum)
//                                .doOnNext(c-> log.info("Total item inserted {}", c))
//                                .doOnError(err -> {
//                                   throw new RuntimeException("Cannot retrive data or save", err);})
//                                .doOnSuccess((result)-> log.info("Save complete in {} ms", Instant.now().toEpochMilli() - start));
//                    );
//    }



    public Mono<Integer> saveBatch() {
        var start = Instant.now().toEpochMilli();
        var insertCount = new AtomicInteger();
        return r2dbcEntityTemplate.getDatabaseClient()
                .inConnectionMany(connection -> getClientFlux()
                        .map(o -> {o.setName("LOL"); return o;})
                        .distinct().buffer(10_000).publishOn(Schedulers.boundedElastic())
                        .flatMap(records -> {
                            final var batch = connection.createBatch();
                            records.forEach(record->batch.add(Client.buildInsertQuery(record)));
                            return batch.execute();
                        })
                ) .flatMap(r -> Objects.isNull(r) ? Flux.just(0): r.getRowsUpdated())
                .doOnNext(n-> {
                    if(insertCount.addAndGet(n) % 100_000 == 0){
                        log.info("Items insted -> {}", insertCount.get());
                    }
                })
                .reduce(0, Integer::sum).doOnNext(c-> log.info("Total item inserted {}", c))
                .doOnError(err -> {
                    throw new RuntimeException("Cannot retrive data or save", err);})
                .doOnSuccess((result)-> log.info("Save complete in {} ms", Instant.now().toEpochMilli() - start));
    }



    public Flux<Client> getClientFlux() {
        return webClient.post()
                .uri("http://localhost:8083/persons/")
                .header("Accept", "application/json", "application/stream-x-jackson-smile", "application/stream-json")
                //.body(BodyInserters.fromValue(RequestGenerator.getRequest()))
                .retrieve()
                .bodyToFlux(Client.class)
                .onErrorContinue((throwable, o) -> log.error("Skipped error while retreiving {}", throwable.getMessage()))
                .doOnComplete(() -> log.info("Retrieval complete"));
    }


}
