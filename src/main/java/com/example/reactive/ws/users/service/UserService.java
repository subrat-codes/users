package com.example.reactive.ws.users.service;

import com.example.reactive.ws.users.models.request.UserRequest;
import com.example.reactive.ws.users.models.response.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {

    Mono<UserResponse> createUser(Mono<UserRequest> userRequestMono);
    Mono<UserResponse> getUserById(UUID id);
    Flux<UserResponse> getUsers(int offset, int limit);
}
