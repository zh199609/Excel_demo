package com.zl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.Set;

@Controller
public class ThymleafController {

    @RequestMapping(value = "/thTest")
    public ModelAndView index() {
        Set<String> set = new HashSet<>();
        set.add("a");
        set.add("b");
        set.add("c");
        set.add("d");
        set.add("e");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("thTest");
        modelAndView.addObject("names", set);
        return modelAndView;
    }

    @RequestMapping(value = "/dataTest")
    public ModelAndView dataTest() {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }
}
