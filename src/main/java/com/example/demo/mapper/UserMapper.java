package com.example.demo.mapper;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User login(@Param("username") String username, @Param("password") String password);
    int register(@Param("username") String username, @Param("password") String password, @Param("is_admin") String is_admin);
    User findByUsername(@Param("username") String username);
}
