package com.example.user.updater.repository;

import com.example.user.updater.repository.entity.Department;
import com.example.user.updater.repository.entity.User;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Testcontainers
@SpringBootTest(properties = {"spring.main.lazy-initialization=true"})
class UserRepositoryIntegrationTest {

    private final static EasyRandom GENERATOR = new EasyRandom();
    private final static Integer USERS_TO_UPDATE_COUNT = 100;
    private static Long DEPARTMENT_ID;

    @SuppressWarnings("resource")
    @Container
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:14-alpine")
            .withDatabaseName("cross_sbp")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL::getPassword);
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        departmentRepository.deleteAll();
        userRoleRepository.deleteAll();

        Department department = new Department();
        department.setName("IT");
        department.setActive(true);
        department = departmentRepository.save(department);
        DEPARTMENT_ID = department.getId();

        List<User> users = new ArrayList<>();
        for (int i = 0; i < USERS_TO_UPDATE_COUNT; i++) {
            User user = GENERATOR.nextObject(User.class);
            user.setDepartment(department);
            user.setUpdateDate(OffsetDateTime.now().minusDays(32));
            users.add(user);
        }
        for (int i = 0; i < 50; i++) {
            User user = GENERATOR.nextObject(User.class);
            user.setDepartment(department);
            user.setUpdateDate(OffsetDateTime.now().minusDays(20));
            users.add(user);
        }
        userRepository.saveAll(users);
    }

    @Test
    void shouldReturnUsersWithCorrectUpdateDate() {
        // when
        List<User> users = userRepository.findByDepartmentIdAndUpdateDateBefore(
                DEPARTMENT_ID, OffsetDateTime.now().minusMonths(1));

        // then
        Assertions.assertThat(users)
                .isNotNull()
                .hasSize(USERS_TO_UPDATE_COUNT);
    }
}