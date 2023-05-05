package com.example.demo.service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User login(String username, String password) {
        return userMapper.login(username, password);
    }

    public int register(String username, String password, String status) {
        return userMapper.register(username, password, status);
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public int updateByTeamname(String ori_team_name,String new_team_name){
        return userMapper.updateByTeamname(ori_team_name, new_team_name);
    }
}
