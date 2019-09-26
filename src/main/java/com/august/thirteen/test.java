package com.august.thirteen;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {

    private  String test(){
        String a="SSSS";
        String intern = a.intern();
        return "hhhhh";
    }
}
