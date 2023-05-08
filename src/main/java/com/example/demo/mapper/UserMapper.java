package com.example.demo.mapper;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User login(@Param("username") String username, @Param("password") String password);
    int register(@Param("username") String username, @Param("password") String password, @Param("status") String status);
    User findByUsername(@Param("username") String username);

    List<User> findByTeamname(@Param("team_name") String team_name);
    int updateByTeamname(@Param("ori_team_name") String ori_team_name,@Param("new_team_name") String new_team_name);

    int updateByTeamnameAndUsername(@Param("ori_team_name") String ori_team_name,@Param("new_team_name") String new_team_name,@Param("username") String username);
    int userJoinIntoTeam(@Param("team_name")String team_name,@Param("username")String username);

    int changeUserScore(@Param("username") String username,@Param("score") int score);
}
