package com.example.demo.controller;

import com.example.demo.commom.AjaxResult;
import com.example.demo.commom.Constant;
import com.example.demo.commom.SessionUtil;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
@Api(tags = "用户控制器")
@RestController
@CrossOrigin(origins = "http://localhost:9528")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @ApiOperation(value = "用户注册", notes = "根据提供的用户名、密码和身份，注册新用户")
    @RequestMapping("/register")
    public <status> HashMap<String, Object> register(@RequestBody String username, String password,String status, HttpServletRequest request) {

        int result = userService.register(username, password, status);
        if (result == 0) {
            return AjaxResult.fail(-1, "Registration failed!");
        }
        // Store the user's information in the session
        User user = userService.login(username, password);
        request.getSession().setAttribute(Constant.SESSION_USERINFO_KEY, user);
        return AjaxResult.success("User registered successfully.");
    }
    @ApiOperation(value = "用户登录", notes = "根据提供的用户名和密码，用户登录")
    @RequestMapping("/login")
    public HashMap<String, Object> login(@RequestBody Map<String, String> userData, HttpServletRequest request) {
        String username = userData.get("username");
        String password = userData.get("password");
        User user = userService.login(username, password);
        if (user == null) {
            return AjaxResult.fail(-1, "Login failed!");
        }
        // Store the user's information in the session
        request.getSession().setAttribute(Constant.SESSION_USERINFO_KEY, user);
        return AjaxResult.success(user);
    }
    @ApiOperation(value = "获取登录用户的状态", notes = "获取当前登录用户的状态")
    @RequestMapping("/getLoginUserStatus")
    public HashMap<String, Object> getStatus(HttpServletRequest request) {
        User user = SessionUtil.getLoginUser(request);
        if (user == null) {
            return AjaxResult.fail(-1, "User not logged in");
        }
        User dbUser = userService.findByUsername(user.getUsername());
        if (dbUser == null) {
            return AjaxResult.fail(-1, "User not found");
        }
        String status = dbUser.getStatus();
        return AjaxResult.success(status);
    }

    @ApiOperation(value = "获取登录用户信息", notes = "获取当前登录用户的信息")
    @RequestMapping("/getLoginUser")
    public HashMap<String, Object> getLoginUser(HttpServletRequest request) {
        User user = SessionUtil.getLoginUser(request);
        if (user == null) {
            return AjaxResult.fail(-1, "User not logged in");
        }
        User dbUser = userService.findByUsername(user.getUsername());
        if (dbUser == null) {
            return AjaxResult.fail(-1, "User not found");
        }
        return AjaxResult.success(dbUser);
    }
    @ApiOperation(value = "修改用户分数", notes = "管理员可以修改用户的分数")
    @RequestMapping("/changeUserScore")
    public HashMap<String, Object> changeUserScore(@RequestBody int score, HttpServletRequest httpServletRequest){
        User user = SessionUtil.getLoginUser(httpServletRequest);
        if (user == null) {
            return AjaxResult.fail(-1, "User not logged in");
        }
        User dbUser = userService.findByUsername(user.getUsername());
        if (dbUser == null) {
            return AjaxResult.fail(-1, "User not found");
        }
        if(!Objects.equals(dbUser.getStatus(), "admin")){
            return AjaxResult.fail(-1,"您没有权限修改队员分数.");
        }
        userService.changeUserScore(dbUser.getUsername(), score);
        return AjaxResult.success(200,"修改成功");
    }
    @ApiOperation(value = "查询队伍中队员", notes = "根据队伍名查询对应队伍的队员")
    @RequestMapping("/searchUsersByTeamName")
    public HashMap<String, Object> searchUsersByTeamName(@RequestBody String team_name){
        System.out.println(team_name);
        if(team_name == null){
            return AjaxResult.fail(-1,"队名不能为空.");
        }
        List<User> users = userService.findByTeamname(team_name);
        return AjaxResult.success(users);
    }
}
