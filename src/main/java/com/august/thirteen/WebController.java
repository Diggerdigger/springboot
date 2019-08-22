package com.august.thirteen;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WebController {

    @GetMapping(value = "/fitst" )
    @ResponseBody
    public String sayHello(){
        return "hello";
    }
}
