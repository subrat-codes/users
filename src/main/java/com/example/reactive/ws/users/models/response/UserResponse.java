package com.example.reactive.ws.users.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;

}
