package com.example.demo.controller;

import com.example.demo.commom.AjaxResult;
import com.example.demo.commom.Constant;
import com.example.demo.commom.SessionUtil;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public HashMap<String, Object> register(@RequestBody Map<String, String> userData, HttpServletRequest request) {
        String username = userData.get("username");
        String password = userData.get("password");
        int result = userService.register(username, password, "N");
        if (result == 0) {
            return AjaxResult.fail(-1, "Registration failed!");
        }
        // Store the user's information in the session
        User user = userService.login(username, password);
        request.getSession().setAttribute(Constant.SESSION_USERINFO_KEY, user);
        return AjaxResult.success("User registered successfully.");
    }

    @PostMapping("/login")
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

    @GetMapping("/is-admin")
    public HashMap<String, Object> isAdmin(HttpServletRequest request) {
        User user = SessionUtil.getLoginUser(request);
        if (user == null) {
            return AjaxResult.fail(-1, "User not logged in");
        }
        User dbUser = userService.findByUsername(user.getUsername());
        if (dbUser == null) {
            return AjaxResult.fail(-1, "User not found");
        }
        boolean isAdmin = "Y".equalsIgnoreCase(dbUser.getIs_admin());
        return AjaxResult.success(isAdmin);
    }
}
