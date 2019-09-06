package com.august.thirteen.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class WebController {

        @GetMapping(value = "/" )
        public String index(){
            return "index";
        }


}



