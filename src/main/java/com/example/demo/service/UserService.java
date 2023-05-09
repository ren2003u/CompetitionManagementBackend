package com.example.demo.service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public int updateByTeamnameAndUsername(String ori_team_name,String new_team_name,String username){
        return userMapper.updateByTeamnameAndUsername(ori_team_name,new_team_name,username);
    }

    public List<User> findByTeamname(String team_name){
        return userMapper.findByTeamname(team_name);
    }

    public int changeUserScore(String username,int score){
        return userMapper.changeUserScore(username,score);
    }

    public List<User> findAllUsers(){
        return userMapper.findAllUsers();
    }

    public int updateUserByUserId(User user){
        return userMapper.updateUserByUserId(user);
    }

    public User findByUserId(int userId){
        return userMapper.findByUserId(userId);
    }
}
