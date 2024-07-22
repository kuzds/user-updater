package com.example.user.updater.controller;

import com.example.user.updater.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/update")
    public void update(@RequestParam("departmentId") Long departmentId) {
        userService.updateUsersByDepartmentId(departmentId);
    }
}
