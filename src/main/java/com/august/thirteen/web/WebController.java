package com.august.thirteen.web;

import com.august.thirteen.mapper.UserMapper;
import com.august.thirteen.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


@Controller
public class WebController {

        @Autowired
        private UserMapper userMapper;

        @GetMapping(value = "/" )
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie c:cookies) {
            if ( c.getName().equals("token")){
                String token = c.getValue();
                System.out.println(token);
                UserModel user=userMapper.findUserByToken(token);
                request.getSession().setAttribute("user",user);
                break;
            }

        }
        return "index";
    }

    @GetMapping(value = "/test" )
    @ResponseBody
    public String test(){
        String s="3333333333333333";
        return s;
    }


}



