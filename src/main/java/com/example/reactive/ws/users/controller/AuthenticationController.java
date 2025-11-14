package com.example.reactive.ws.users.controller;

import com.example.reactive.ws.users.models.request.AuthenticateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AuthenticationController {

    @PostMapping("/login")
    public Mono<ResponseEntity<Void>> login(@RequestBody Mono<AuthenticateUserRequest> authenticateUserRequest) {
        return Mono.just(ResponseEntity.ok().build());
    }

}
