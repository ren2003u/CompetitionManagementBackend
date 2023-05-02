package com.example.demo.commom;

import java.util.HashMap;

public class AjaxResult {
    public static HashMap<String,Object> success(int code, String msg){
        HashMap<String,Object> result = new HashMap<>();
        result.put("code",code);
        result.put("msg",msg);
        result.put("data","");
        return result;
    }
    public static HashMap<String,Object> success(Object data){
        HashMap<String,Object> result = new HashMap<>();
        result.put("code",200);
        result.put("msg","");
        result.put("data",data);
        return result;
    }

    public static HashMap<String,Object> fail(int code,String msg){
        HashMap<String,Object> result = new HashMap<>();
        result.put("code",code);
        result.put("msg",msg);
        result.put("data","");
        return result;
    }
}
