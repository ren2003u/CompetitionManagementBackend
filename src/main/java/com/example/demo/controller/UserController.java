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
//@CrossOrigin(origins = "http://localhost:9528")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @ApiOperation(value = "用户注册", notes = "根据提供的用户名、密码和身份，注册新用户")
    @RequestMapping("/register")
    public <status> HashMap<String, Object> register(@RequestParam("username") String username, @RequestParam("password")String password,@RequestParam("status")String status, HttpServletRequest request) {

        if(userService.findByUsername(username) != null){
            return AjaxResult.fail(-1,"用户名已存在，注册失败");
        }
        int result = userService.register(username, password, status);
        if (result == 0) {
            return AjaxResult.fail(-1, "注册失败!");
        }
        // Store the user's information in the session
        User user = userService.login(username, password);
        request.getSession().setAttribute(Constant.SESSION_USERINFO_KEY, user);
        return AjaxResult.success("用户注册成功.");
    }
    @ApiOperation(value = "用户登录", notes = "根据提供的用户名和密码，用户登录")
    @RequestMapping("/login")
    public HashMap<String, Object> login(@RequestParam("username") String username, @RequestParam("password")String password, HttpServletRequest request) {
        User user = userService.login(username, password);
        if (user == null) {
            return AjaxResult.fail(-1, "登录失败!");
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
            return AjaxResult.fail(-1, "用户未登录");
        }
        User dbUser = userService.findByUsername(user.getUsername());
        if (dbUser == null) {
            return AjaxResult.fail(-1, "用户非法登录");
        }
        String status = dbUser.getStatus();
        return AjaxResult.success(status);
    }

    @ApiOperation(value = "获取所有用户", notes = "获取数据库中所有用户信息")
    @RequestMapping("/getAllUsers")
    public HashMap<String, Object> getAllUsers(HttpServletRequest request) {
        User user = SessionUtil.getLoginUser(request);
        if (user == null) {
            return AjaxResult.fail(-1, "用户未登录");
        }
        User dbUser = userService.findByUsername(user.getUsername());
        if (dbUser == null) {
            return AjaxResult.fail(-1, "用户非法登录");
        }
        List<User> userList = userService.findAllUsers();
        return AjaxResult.success(userList);
    }

    @ApiOperation(value = "获取登录用户信息", notes = "获取当前登录用户的信息")
    @RequestMapping("/getLoginUser")
    public HashMap<String, Object> getLoginUser(HttpServletRequest request) {
        User user = SessionUtil.getLoginUser(request);
        if (user == null) {
            return AjaxResult.fail(-1, "用户未登录");
        }
        User dbUser = userService.findByUsername(user.getUsername());
        if (dbUser == null) {
            return AjaxResult.fail(-1, "用户非法登录");
        }
        return AjaxResult.success(dbUser);
    }
    @ApiOperation(value = "修改用户分数", notes = "管理员修改用户的分数")
    @RequestMapping("/changeUserScore")
    public HashMap<String, Object> changeUserScore(@RequestParam("score") int score, @RequestParam("username") String username,HttpServletRequest httpServletRequest){
        User user = SessionUtil.getLoginUser(httpServletRequest);
        if (user == null) {
            return AjaxResult.fail(-1, "用户未登录");
        }
        User dbUser = userService.findByUsername(user.getUsername());
        if (dbUser == null) {
            return AjaxResult.fail(-1, "用户非法登录");
        }
        if(!Objects.equals(dbUser.getStatus(), "admin")){
            return AjaxResult.fail(-1,"您没有权限修改队员分数.");
        }
        userService.changeUserScore(username, score);
        return AjaxResult.success(200,"修改成功");
    }
    @ApiOperation(value = "查询队伍中队员", notes = "根据队伍名查询对应队伍的队员")
    @RequestMapping("/searchUsersByTeamName")
    public HashMap<String, Object> searchUsersByTeamName(@RequestParam("team_name") String team_name){
        System.out.println(team_name);
        if(team_name == null){
            return AjaxResult.fail(-1,"队名不能为空.");
        }
        List<User> users = userService.findByTeamname(team_name);
        return AjaxResult.success(users);
    }

    @ApiOperation(value = "修改用户个人信息", notes = "用户修改自己个人信息")
    @RequestMapping("/changeUserInfor")
    public HashMap<String, Object> updateUserByUserId(@RequestBody User user,HttpServletRequest httpServletRequest){
        User user_1 = SessionUtil.getLoginUser(httpServletRequest);
        if (user_1 == null) {
            return AjaxResult.fail(-1, "用户未登录");
        }
        User dbUser = userService.findByUsername(user_1.getUsername());
        if (dbUser == null) {
            return AjaxResult.fail(-1, "用户非法登录");
        }
        if(!Objects.equals(userService.findByUserId(user.getId()).getUsername(), user.getUsername()) && userService.findByUsername(user.getUsername()) != null){
            return AjaxResult.fail(-1,"您要修改的用户名已经存在");
        }
        userService.updateUserByUserId(user);
        return AjaxResult.success(200,"修改成功");
    }
}
