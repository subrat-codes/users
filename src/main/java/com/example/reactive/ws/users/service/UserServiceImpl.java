package com.example.reactive.ws.users.service;

import com.example.reactive.ws.users.data.UserEntity;
import com.example.reactive.ws.users.data.UserRepository;
import com.example.reactive.ws.users.models.request.UserRequest;
import com.example.reactive.ws.users.models.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<UserResponse> createUser(Mono<UserRequest> userRequestMono) {
        log.info("Create user called : {}", userRequestMono);
        return userRequestMono
                .mapNotNull(this::convertUserRequestToUserEntity)
                .flatMap(userRepository::save)
                .mapNotNull(this::convertUserEntityToUserResponse)
                .onErrorMap(DuplicateKeyException.class,
                        exception -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                exception.getMessage()));
    }

    @Override
    public Mono<UserResponse> getUserById(UUID id) {
        return userRepository.findById(id).mapNotNull(this::convertUserEntityToUserResponse);
    }

    @Override
    public Flux<UserResponse> getUsers(int pageNo, int limit) {
        if (pageNo > 0) pageNo = pageNo - 1;
        Pageable pageable = PageRequest.of(pageNo, limit);
        return userRepository.findAllBy(pageable).map(this::convertUserEntityToUserResponse);
    }

    private UserEntity convertUserRequestToUserEntity(UserRequest userRequest) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userRequest, userEntity);
        return userEntity;
    }

    private UserResponse convertUserEntityToUserResponse(UserEntity userEntity) {
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(userEntity, userResponse);
        return userResponse;
    }

}
