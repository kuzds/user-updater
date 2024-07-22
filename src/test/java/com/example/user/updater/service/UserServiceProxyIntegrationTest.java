package com.example.user.updater.service;

import com.example.user.updater.extservice.ExtServiceClient;
import com.example.user.updater.repository.UserRepository;
import com.example.user.updater.repository.entity.User;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.times;

@SpringBootTest(properties = {"spring.main.lazy-initialization=true"})
class UserServiceProxyIntegrationTest {

    private final static EasyRandom GENERATOR = new EasyRandom();

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ExtServiceClient extServiceClient;

    @Test
    void update_shouldMakeRetries() {
        // given
        String login = "login";
        User user = GENERATOR.nextObject(User.class);
        user.setLogin(login);

        Mockito.when(extServiceClient.getUser(login)).thenThrow(new RuntimeException());

        //when
        Assertions.assertThatThrownBy(() -> userService.update(user))
                .isInstanceOf(RuntimeException.class);

        // then
        Mockito.verify(extServiceClient, times(3)).getUser(login);
    }
}