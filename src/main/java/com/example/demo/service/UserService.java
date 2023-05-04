package com.example.demo.service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User login(String username, String password) {
        return userMapper.login(username, password);
    }

    public int register(String username, String password, String is_admin) {
        return userMapper.register(username, password, is_admin);
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}
