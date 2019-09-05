package com.zl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;

@RestController
public class RandomTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping(value = "playSelect")
    @ResponseBody
    public String playSelect() {
        List<String> play = redisTemplate.opsForSet().randomMembers("play", 1);
        return play.get(0);
    }

    @RequestMapping(value = "addPlay/{name}")
    @ResponseBody
    public String addPlay(@PathVariable("name") String playName) {
        redisTemplate.opsForSet().add("play", playName);
        return playName;
    }

    @RequestMapping(value = "/eatSelect")
    @ResponseBody
    public String eatSelect() {
        List<String> play = redisTemplate.opsForSet().randomMembers("eat", 1);
        return play.get(0);
    }

    @RequestMapping(value = "addEat/{name}")
    @ResponseBody
    public String addEat(@PathVariable("name") String eatName) {
        redisTemplate.opsForSet().add("eat", eatName);
        return eatName;
    }


    @RequestMapping(value = "/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        Set<String> play = redisTemplate.opsForSet().members("play");
        modelAndView.addObject("playNames", play);
        modelAndView.setViewName("random");
        return modelAndView;
    }


}
