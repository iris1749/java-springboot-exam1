package com.korea.textboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class test {

    @RequestMapping("/hello")
    public  String hello(){
        return "test";
    }
}
