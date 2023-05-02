package com.example.demo.commom;

import com.example.demo.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {
    public static User getLoginUser(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session!=null && session.getAttribute(Constant.SESSION_USERINFO_KEY)!=null){
            return (User) session.getAttribute(Constant.SESSION_USERINFO_KEY);
        }
        return null;
    }
}