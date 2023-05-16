package com.webflux.project.controllers;

import com.webflux.project.models.Client;
import com.webflux.project.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final ClientRepository clientRepository;


    @GetMapping
    public Flux<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Client>> getClientById(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Client> createClient(@RequestBody Client product) {
        return clientRepository.save(product);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Client>> updateClient(@PathVariable Long id, @RequestBody Client product) {
        return clientRepository.findById(id)
                .flatMap(existingClient -> {
                    existingClient.setName(product.getName());
                    return clientRepository.save(existingClient);
                })
                .map(updatedClient -> ResponseEntity.ok(updatedClient))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteClient(@PathVariable Long id) {
        return clientRepository.findById(id)
                .flatMap(existingClient ->
                        clientRepository.delete(existingClient)
                                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
