package com.example.reactive.ws.users.service;

import com.example.reactive.ws.users.data.UserEntity;
import com.example.reactive.ws.users.data.UserRepository;
import com.example.reactive.ws.users.models.request.UserRequest;
import com.example.reactive.ws.users.models.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserResponse> createUser(Mono<UserRequest> userRequestMono) {
        log.info("Create user called : {}", userRequestMono);
        return userRequestMono
                .flatMap(this::convertUserRequestToUserEntity)
                .flatMap(userRepository::save)
                .mapNotNull(this::convertUserEntityToUserResponse);
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

    private Mono<UserEntity> convertUserRequestToUserEntity(UserRequest userRequest) {
        return Mono.fromCallable(() -> {
            UserEntity userEntity = new UserEntity();
            BeanUtils.copyProperties(userRequest, userEntity);
            userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            return userEntity;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private UserResponse convertUserEntityToUserResponse(UserEntity userEntity) {
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(userEntity, userResponse);
        return userResponse;
    }

}
