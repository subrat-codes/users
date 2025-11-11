package com.example.reactive.ws.users.controller;

import com.example.reactive.ws.users.models.request.UserRequest;
import com.example.reactive.ws.users.models.response.UserResponse;
import com.example.reactive.ws.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<UserResponse>> createUser(
            @RequestBody @Valid Mono<UserRequest> createUserRequest) {

        log.info("Create user called : {}", createUserRequest);

        return userService.createUser(createUserRequest)
                .map(userResponse ->
                        ResponseEntity.status(HttpStatus.CREATED)
                                .location(URI.create("/users/" + userResponse.getId()))
                                .body(userResponse));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> getUser(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(userResponse ->
                        ResponseEntity.status(HttpStatus.OK).body(userResponse))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @GetMapping
    public Flux<UserResponse> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "limit", defaultValue = "50") int limit) {
        log.info("page: {}, limit: {}", page, limit);
        return userService.getUsers(page, limit);
    }

}
