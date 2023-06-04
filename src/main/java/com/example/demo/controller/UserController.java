package com.example.demo.controller;

import com.example.demo.auth.jwt.JwtUtil;
import com.example.demo.commom.AjaxResult;
import com.example.demo.commom.Constant;
import com.example.demo.commom.SessionUtil;
import com.example.demo.config.CustomUserDetailsService;
import com.example.demo.model.User;
import com.example.demo.service.TeamInformationService;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private TeamInformationService teamInformationService;

    @RequestMapping("/register")
    public HashMap<String, Object> register(@RequestParam("username") String username, @RequestParam("password")String password,@RequestParam("status")String status, HttpServletRequest request) {

        if(StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(status)){
            return AjaxResult.fail(-1,"传输的注册信息不完整");
        }
        if(userService.findByUsername(username) != null){
            return AjaxResult.fail(-1,"用户名已存在，注册失败");
        }

        String encodedPassword = passwordEncoder.encode(password);
        int result = userService.register(username, encodedPassword, status);

        if (result == 0) {
            return AjaxResult.fail(-1, "注册失败!");
        }

        // Store the user's information in the session
        User user = userService.login(username, encodedPassword);
        request.getSession().setAttribute(Constant.SESSION_USERINFO_KEY, user);

        return AjaxResult.success("用户注册成功.");
    }

    @RequestMapping("/login")
    public HashMap<String, Object> login(@RequestParam("username") String username, @RequestParam("password")String password, HttpServletRequest request) {
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return AjaxResult.fail(-1,"传输的登录信息不完整");
        }
        User user = userService.findByUsername(username);

        if (user == null || !(passwordEncoder.matches(password, user.getPassword()))) {
            return AjaxResult.fail(-1, "登录失败!");
        }

        // Generate the JWT token using the user details
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        String token = jwtUtil.generateToken(userDetails);

        // Store the user's information in the session
        request.getSession().setAttribute(Constant.SESSION_USERINFO_KEY, user);

        // Include the JWT token in the response
        HashMap<String, Object> response = AjaxResult.success(user);
        response.put("token", token);
        return response;
    }

    @ApiOperation(value = "获取登录用户的状态", notes = "获取当前登录用户的状态")
    @RequestMapping("/getLoginUserStatus")
    public HashMap<String, Object> getStatus(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constant.SESSION_USERINFO_KEY);
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
        User user = (User) request.getSession().getAttribute(Constant.SESSION_USERINFO_KEY);
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
        User user = (User) request.getSession().getAttribute(Constant.SESSION_USERINFO_KEY);
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
    public HashMap<String, Object> changeUserScore(@RequestParam("score") int score, @RequestParam("username") String username,HttpServletRequest request){
        if(StringUtils.isBlank(username) || score < 0){
            return AjaxResult.fail(-1,"传输的信息不完整或存在非法信息");
        }
        if(userService.findByUsername(username) == null){
            return AjaxResult.fail(-1,"传输的用户名没有用户与其对应");
        }
        User user = (User) request.getSession().getAttribute(Constant.SESSION_USERINFO_KEY);
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
        if(StringUtils.isBlank(team_name)){
            return AjaxResult.fail(-1,"队名不能为空");
        }
        if(teamInformationService.findTeamByName(team_name) == null){
            return AjaxResult.fail(-1,"传输的队名没有队伍与其对应");
        }
        List<User> users = userService.findByTeamname(team_name);
        return AjaxResult.success(users);
    }

    @ApiOperation(value = "修改用户个人信息", notes = "用户修改自己个人信息")
    @RequestMapping("/changeUserInfor")
    public HashMap<String, Object> updateUserByUserId(@RequestBody User user,HttpServletRequest request){
        if(user.getTeam_name() == null){
            return AjaxResult.fail(-1,"传输的队伍名称非法");
        }
        if(!user.getTeam_name().equals("")){
            if(teamInformationService.findTeamByName(user.getTeam_name()) == null){
                return AjaxResult.fail(-1,"传输的队名没有队伍与其对应");
            }
        }
        User user_1 = (User) request.getSession().getAttribute(Constant.SESSION_USERINFO_KEY);
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
