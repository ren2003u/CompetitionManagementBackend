package com.example.demo.mapper;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User login(@Param("username") String username, @Param("password") String password);
    int register(@Param("username") String username, @Param("password") String password, @Param("status") String status);
    User findByUsername(@Param("username") String username);

    int updateByTeamname(@Param("ori_team_name") String ori_team_name,@Param("new_team_name") String new_team_name);

    int userJoinIntoTeam(String)
}
