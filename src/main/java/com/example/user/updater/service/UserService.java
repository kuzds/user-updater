package com.example.user.updater.service;

import com.example.user.updater.extservice.ExtServiceClient;
import com.example.user.updater.extservice.dto.ExternalServiceDto;
import com.example.user.updater.repository.UserRepository;
import com.example.user.updater.repository.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private static final Integer CHUNK_SIZE = 20;

    private final UserRepository userRepository;
    private final ExtServiceClient extServiceClient;

    @Lookup
    public UserService self() {
        return null;
    }

    @Transactional
    public void updateUsersByDepartmentId(Long departmentId) {
        List<User> users = userRepository.findByDepartmentIdAndUpdateDateBefore(
                departmentId,
                OffsetDateTime.now().minusMonths(1)
        );

        for (int i = 0; i < users.size(); i += CHUNK_SIZE) {
            List<User> chunk = users.subList(i, Math.min(i + CHUNK_SIZE, users.size()));
            self().processUsersChunk(chunk);
        }
    }

    // https://stackoverflow.com/a/75676138
    @Retryable(maxAttempts = 3, retryFor = Exception.class)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processUsersChunk(List<User> users) {
        users.forEach(self()::update);
        userRepository.saveAll(users);
    }

    @Retryable(maxAttempts = 3, retryFor = Exception.class)
    public void update(User user) {
        ExternalServiceDto dto = extServiceClient.getUser(user.getLogin());

        if (Objects.nonNull(dto)) {
            user.setFullName(dto.getFullName());
            //user.set(dto.getPhoneNumber()); //todo user do not have phoneNumber
            user.setFullName(dto.getEmail());
        } else {
            log.warn("External service return null user with login: {}", user.getLogin());
        }
    }
}
