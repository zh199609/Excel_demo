package com.zl.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RandomTest {

    @RequestMapping(value = "playSelect")
    public String playSelect(){
        return "";
    }

    @RequestMapping(value = "/eatSelect")
    public String eatSelect(){
        return "";
    }

}
