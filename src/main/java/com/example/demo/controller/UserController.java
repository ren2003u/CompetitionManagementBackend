package com.example.demo.controller;

import com.example.demo.commom.AjaxResult;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public HashMap register(@RequestParam("username") String username, @RequestParam("password") String password) {
        int result = userService.register(username, password, "N");
        if (result == 0) {
            return AjaxResult.fail(-1, "Registration failed!");
        }
        return AjaxResult.success("User registered successfully.");
    }

    @PostMapping("/login")
    public HashMap login(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = userService.login(username, password);
        if (user == null) {
            return AjaxResult.fail(-1, "Login failed!");
        }
        return AjaxResult.success(user);
    }
}
