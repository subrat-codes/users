package com.example.reactive.ws.users.models.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthenticateUserRequest {
    private String email;
    private String password;
}
